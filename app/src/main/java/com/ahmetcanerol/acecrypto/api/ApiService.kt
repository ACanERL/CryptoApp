package com.ahmetcanerol.acecrypto.api


import com.ahmetcanerol.acecrypto.Constants
import com.ahmetcanerol.acecrypto.Constants.BASE_URL
import com.ahmetcanerol.acecrypto.model.CoinListModel
import io.reactivex.Single

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val client = OkHttpClient
        .Builder()
        .build()
    private var api= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(Api::class.java)

    fun getData(): Single<List<CoinListModel>>
    {
        return api.getCoinsApi("usd","market_cap_des",50,true,"1h")
    }

    fun getTop_5():Single<List<CoinListModel>>{
        return  api.getCoinsApi("usd","market_cap_des",5,true,"24h")
    }

    object ApiClient {
        fun getApiService(): Api {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofitBuilder.create(Api::class.java)
        }
    }

}