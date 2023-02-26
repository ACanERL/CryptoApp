package com.ahmetcanerol.acecrypto.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "user_wallet")
data class UserCoins(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var coinsName:String,
    var coinQu:String
)
