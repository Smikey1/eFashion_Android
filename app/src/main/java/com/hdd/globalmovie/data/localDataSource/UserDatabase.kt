package com.hdd.globalmovie.data.localDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hdd.globalmovie.data.models.User

@Database(entities= [(User::class)],version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile // it make all instance available to all coroutines threats
        private var instance: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {

            if (instance == null) {
                // it(synchronized) block all the thread and allows to create database instance
                synchronized(UserDatabase::class) {
                    instance = createDatabase(context)
                }
            }
            return instance!!
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, UserDatabase::class.java, "UserDatabase")
                .build()
    }
}