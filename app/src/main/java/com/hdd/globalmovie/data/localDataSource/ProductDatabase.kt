package com.hdd.globalmovie.data.localDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hdd.globalmovie.data.models.ListToStringConverter
import com.hdd.globalmovie.data.models.Product
import com.hdd.globalmovie.data.models.User

@Database(entities= [(User::class),(Product::class)],version = 1,exportSchema = false)
@TypeConverters(ListToStringConverter::class)

abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO

    companion object {
        @Volatile // it make all instance available to all coroutines threats
        private var instance: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase {

            if (instance == null) {
                // it(synchronized) block all the thread and allows to create database instance
                synchronized(ProductDatabase::class) {
                    instance = createDatabase(context)
                }
            }
            return instance!!
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "ProductDatabase")
                .build()
    }
}