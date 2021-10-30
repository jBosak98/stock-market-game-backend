import com.ktor.stock.market.game.jbosak.model.SingleCandle

data class DataFetcherReponse(
    val ticker:String,
    val candles:Array<SingleCandle>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataFetcherReponse

        if (ticker != other.ticker) return false
        if (!candles.contentEquals(other.candles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ticker.hashCode()
        result = 31 * result + candles.contentHashCode()
        return result
    }
}