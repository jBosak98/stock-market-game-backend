import arrow.core.getOrElse
import com.finnhub.api.models.Quote
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import com.ktor.stock.market.game.jbosak.repository.QuoteRepository
import com.ktor.stock.market.game.jbosak.service.rabbitConnectionFactory
import com.ktor.stock.market.game.jbosak.utils.dateTimeConverter
import com.rabbitmq.client.*
import io.ktor.config.*
import io.ktor.util.*
import org.joda.time.DateTime


@OptIn(KtorExperimentalAPI::class)
fun deliverCallback(config: ApplicationConfig, channel: Channel): DeliverCallback =
    DeliverCallback { consumerTag: String?, message: Delivery? ->
        val messageBodyString = String(message!!.body)
        println("[Rabbit] - Consuming message!: $messageBodyString")

        val gson = GsonBuilder().registerTypeAdapter(DateTime::class.java, dateTimeConverter).create()
        val response = gson.fromJson<DataFetcherReponse>(messageBodyString, DataFetcherReponse::class.java)
        val ticker = response.ticker.toUpperCase()
        val candles = response.candles.toList()
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse {
                CompanyRepository.insert(ticker)
                CompanyRepository
                    .findCompany(ticker = ticker)
                    .getOrElse { throw Exception("Company $ticker not found") }
            }
            .id

        val quote = candles.last().let { lastCandle ->
            val previousDayClose = candles.findLast {
                it.time.withTimeAtStartOfDay().millis == lastCandle.time.minusDays(1).withTimeAtStartOfDay().millis
            }?.closePrice
            Quote(
                o = lastCandle.openPrice,
                h = lastCandle.highPrice,
                l = lastCandle.lowPrice,
                c = lastCandle.closePrice,
                pc = previousDayClose
            )
        }
        QuoteRepository.insert(quote, companyId)
        CandleRepository.upsert(candles, CandlesResolution.FIFTEEN_MINUTES, companyId)
        val predictionRoutingKey = config.property("ktor.deployment.predictionExchangeRoutingKey").getString()
        val defaultExchange = ""
        val weekAgoDate = DateTime.now().minusWeeks(1)
        val data = CandleRepository.find(ticker, CandlesResolution.FIFTEEN_MINUTES, weekAgoDate)

        val byteData = Gson().toJson(data).toByteArray()

        channel
            .basicPublish(defaultExchange, predictionRoutingKey, null, byteData)

    }

@OptIn(KtorExperimentalAPI::class)
fun rabbit(config:ApplicationConfig) {
    val rabbitHost = config.property("ktor.deployment.rabbitHost").getString()

    val channel = rabbitConnectionFactory(rabbitHost).newConnection().createChannel()
    channel.queueDeclare("prediction", true, false, false, emptyMap())
    val cancelCallback = CancelCallback{consumerTag: String? ->
        println("[Rabbit] - Cancelled... $consumerTag")
    }
    channel.basicConsume("TickerData", deliverCallback(config, channel), cancelCallback)
}