package com.example.losalcesfc.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.losalcesfc.data.dao.SocioDao
import com.example.losalcesfc.data.model.Socio


@Database(
    entities = [Socio::class],
    version = 2,
    exportSchema = false
)
abstract class SocioDatabase : RoomDatabase() {

    abstract fun socioDao(): SocioDao

    companion object {
        @Volatile
        private var INSTANCE: SocioDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE socios ADD COLUMN fotoPath TEXT")
            }
        }

        fun getDatabase(context: Context): SocioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocioDatabase::class.java,
                    "socio_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

