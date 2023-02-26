package com.ahmetcanerol.acecrypto.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ahmetcanerol.acecrypto.local.UserWDatabase
import com.ahmetcanerol.acecrypto.local.repo.UserWRepository
import com.ahmetcanerol.acecrypto.model.UserCoins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserCViewModel(application: Application):AndroidViewModel(application) {
    val readAllData: LiveData<List<UserCoins>>
    private val userRepository:UserWRepository

    init {
        val userCDao= UserWDatabase.getDatabase(application).userCDao()
        userRepository= UserWRepository(userCDao)
        readAllData=userCDao.readAllData()
    }
    fun addUser(user:UserCoins){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }

    }
    fun updateUser(user:UserCoins){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }
    fun deleteUser(user:UserCoins){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }
}