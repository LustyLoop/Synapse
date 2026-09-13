package com.example.synapseapp.room

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase

@Database(
    entities = [DrawerItemsEntity::class],
    version = 1
)
abstract class MainDb: RoomDatabase() {
abstract val dao: Dao
    companion object{
        fun createDataBase(context: Context): MainDb{
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                name = "test.db"
            ).build()
        }
    }
}