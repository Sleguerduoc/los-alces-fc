package com.example.losalcesfc.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.losalcesfc.data.dao.SocioDao
import com.example.losalcesfc.data.model.Socio

@Database(
    entities = [Socio::class],
    version = 1,
    exportSchema = false
)
abstract class SocioDatabase : RoomDatabase() {

    abstract fun socioDao(): SocioDao

    companion object {
        @Volatile
        private var INSTANCE: SocioDatabase? = null

        fun getDatabase(context: Context): SocioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocioDatabase::class.java,
                    "socio_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


