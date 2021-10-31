import arrow.core.getOrElse
import com.finnhub.api.models.Quote
import com.google.gson.GsonBuilder
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
import com.ktor.stock.market.game.jbosak.repository.CompanyRepository
import com.ktor.stock.market.game.jbosak.repository.QuoteRepository
import com.ktor.stock.market.game.jbosak.service.rabbitConnectionFactory
import com.ktor.stock.market.game.jbosak.utils.dateTimeConverter
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import org.joda.time.DateTime

val deliverCallback = DeliverCallback { consumerTag: String?, message: Delivery? ->
    val messageBodyString = String(message!!.body)
    println("[Rabbit] - Consuming message!: $messageBodyString")

        val gson = GsonBuilder().registerTypeAdapter(DateTime::class.java,dateTimeConverter).create()
        val response = gson.fromJson<DataFetcherReponse>(messageBodyString, DataFetcherReponse::class.java)
        val ticker = response.ticker.toUpperCase()
        val candles = response.candles.toList()
        val companyId = CompanyRepository
            .findCompany(ticker = ticker)
            .getOrElse {
                CompanyRepository.insert(ticker)
                CompanyRepository
                    .findCompany(ticker = ticker)
                    .getOrElse{ throw Exception("Company $ticker not found") }
            }
            .id

        val quote = candles.last().let { lastCandle ->
            val previousDayClose = candles.findLast {
                it.time.withTimeAtStartOfDay().millis == lastCandle.time.minusDays(1).withTimeAtStartOfDay().millis
            }?.closePrice
            Quote(
                o=lastCandle.openPrice,
                h=lastCandle.highPrice,
                l=lastCandle.lowPrice,
                c=lastCandle.closePrice,
                pc=previousDayClose
            )
        }
        QuoteRepository.insert(quote, companyId)
        CandleRepository.upsert(candles,CandlesResolution.FIFTEEN_MINUTES, companyId)
}

fun rabbit() {
    val channel = rabbitConnectionFactory.newConnection().createChannel()

    val cancelCallback = CancelCallback{consumerTag: String? ->
        println("[Rabbit] - Cancelled... $consumerTag")
    }
    channel.basicConsume("TickerData", deliverCallback, cancelCallback)
}