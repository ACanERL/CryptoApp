package com.ahmetcanerol.acecrypto.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmetcanerol.acecrypto.model.UserCoins

@Database(entities = [UserCoins::class], version = 1, exportSchema = false)
abstract class UserWDatabase:RoomDatabase(){
    abstract fun userCDao():CDao
    companion object{
        @Volatile
        private var INSTANCE: UserWDatabase? = null


        fun getDatabase(context: Context): UserWDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also {
                        INSTANCE = it }
            }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserWDatabase::class.java, "user_wl"
            ).build()

    }
}