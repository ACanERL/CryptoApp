package com.ahmetcanerol.acecrypto.repository

import com.ahmetcanerol.acecrypto.api.ApiService

class CoinListRepository() {
    val apiService= ApiService()
    fun getData()=apiService.getData()
    fun getTop_5()=apiService.getTop_5()
}