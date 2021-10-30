import com.google.gson.GsonBuilder
import com.ktor.stock.market.game.jbosak.model.CandlesResolution
import com.ktor.stock.market.game.jbosak.model.DataFetcherReponse
import com.ktor.stock.market.game.jbosak.repository.CandleRepository
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
    val (ticker, candles) = response
    CandleRepository.upsert(candles.toList(),ticker.toUpperCase(),CandlesResolution.FIFTEEN_MINUTES)
    println(response)

}

fun rabbit() {
    val channel = rabbitConnectionFactory.newConnection().createChannel()

    val cancelCallback = CancelCallback{consumerTag: String? ->
        println("[Rabbit] - Cancelled... $consumerTag")
    }
    channel.basicConsume("TickerData", deliverCallback, cancelCallback)
}