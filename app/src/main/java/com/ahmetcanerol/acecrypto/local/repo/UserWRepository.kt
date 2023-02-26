package com.ahmetcanerol.acecrypto.local.repo

import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Dao
import com.ahmetcanerol.acecrypto.local.CDao
import com.ahmetcanerol.acecrypto.model.UserCoins

class UserWRepository(private val dao:CDao) {
    fun addUser(user:UserCoins){
        viewModelFactory {
            dao.addUserC(user)
        }
    }
    fun updateUser(user:UserCoins){
        viewModelFactory {
            dao.updateUserC(user)
        }
    }

    fun deleteUser(user:UserCoins){
        viewModelFactory {
            dao.deleteUserC(user )
        }
    }
}