package com.ahmetcanerol.acecrypto.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.ahmetcanerol.acecrypto.model.UserCoins

@Dao
interface CDao {
    @Query(value = "SELECT * FROM user_wallet")
    fun readAllData() : LiveData<List<UserCoins>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUserC(userc: UserCoins)

    @Update
    fun updateUserC(userc: UserCoins)

    @Delete
    fun deleteUserC(userc: UserCoins)
}