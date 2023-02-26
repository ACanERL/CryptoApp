package com.ahmetcanerol.acecrypto.model
data class CoinListModel(
    var id:String,
    var symbol:String,
    var name:String,
    var image:String,
    var current_price:Double,
    var total_volume:Double,
    var double: Double,
    var marketcaprank:Int,
    var high_24h:Double,
    var low_24h:Double,
    var price_change_24h:Double,
    var price_change_percentage_24h:Double,
    var price_change_percentage_1h_in_currency:Double,
    var sparkLine7day: SparkLine7day
)
