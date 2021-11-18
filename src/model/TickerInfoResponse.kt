package com.ktor.stock.market.game.jbosak.model

import com.google.gson.annotations.SerializedName

data class TickerInfoResponse (
    @SerializedName("country") val country : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("exchange") val exchange : String,
    @SerializedName("ipo") val ipo : String,
    @SerializedName("name") val name : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("sharesOutstanding") val sharesOutstanding : Float,
    @SerializedName("ticker") val ticker : String,
    @SerializedName("logo") val logo : String,
    @SerializedName("industry") val industry : String,
    @SerializedName("fiftyTwoWeekLow") val fiftyTwoWeekLow : Double?,
    @SerializedName("beta") val beta : Double?,
    @SerializedName("bookValuePerShareAnnual") val bookValuePerShareAnnual : Double?,
    @SerializedName("bookValuePerShareQuarterly") val bookValuePerShareQuarterly : Double?,
    @SerializedName("bookValueShareGrowth5Y") val bookValueShareGrowth5Y : Double?,
    @SerializedName("dividendPerShareAnnual") val dividendPerShareAnnual : Double?,
    @SerializedName("epsGrowth5Y") val epsGrowth5Y : Double?,
    @SerializedName("marketCapitalization") val marketCapitalization : Double?,
    @SerializedName("epsNormalizedAnnual") val epsNormalizedAnnual : Double?,
    @SerializedName("totalDebtOverTotalEquityQuarterly") val totalDebtOverTotalEquityQuarterly : Double?,
    @SerializedName("website") val website : String,
    @SerializedName("business_summary") val business_summary : String,
    @SerializedName("city") val city : String
)