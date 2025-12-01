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
    version = 3,
    exportSchema = false
)
abstract class SocioDatabase : RoomDatabase() {

    abstract fun socioDao(): SocioDao

    companion object {

        // Versión 1 -> 2: agregar fotoPath
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE socios ADD COLUMN fotoPath TEXT")
            }
        }

        // Versión 2 -> 3: agregar domicilio y coordenadas
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE socios ADD COLUMN domicilio TEXT")
                db.execSQL("ALTER TABLE socios ADD COLUMN latitud REAL")
                db.execSQL("ALTER TABLE socios ADD COLUMN longitud REAL")
            }
        }

        @Volatile
        private var INSTANCE: SocioDatabase? = null

        fun getInstance(context: Context): SocioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SocioDatabase::class.java,
                    "socio_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

