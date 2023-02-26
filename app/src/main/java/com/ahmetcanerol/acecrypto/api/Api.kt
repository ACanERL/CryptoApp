package com.ahmetcanerol.acecrypto.api

import androidx.lifecycle.Observer
import com.ahmetcanerol.acecrypto.model.CoinDailyModel
import com.ahmetcanerol.acecrypto.model.CoinListModel

import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("api/v3/coins/markets")
    fun getCoinsApi(
        @Query("vs_currency")vs_currency:String,
        @Query("order")order:String,
        @Query("per_page")per_page:Int,
        @Query("sparkline")sparkline:Boolean,
        @Query("price_change_percentage")price_change_percentage:String
    ): Single<List<CoinListModel>>

    @GET("api/v3/coins/{id}/market_chart")
    fun getDaily(
        @Path("id")id:String,
        @Query("vs_currency")vs_currency: String,
        @Query("days")days:String
    ):Call<CoinDailyModel>

}