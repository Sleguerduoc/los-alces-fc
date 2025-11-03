package com.example.losalcesfc.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.losalcesfc.data.dao.UsuarioDao
import com.example.losalcesfc.data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Usuario::class],
    version = 4,              // 👈 SUBE versión
    exportSchema = false
)
abstract class UsuarioDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var INSTANCE: UsuarioDatabase? = null

        fun get(context: Context): UsuarioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsuarioDatabase::class.java,
                    "usuarios_db"
                )
                    .fallbackToDestructiveMigration() // 👈 borra y recrea (simple)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                seedAdmin(context)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun seedAdmin(context: Context) {
            val dao = get(context).usuarioDao()
            val admin = Usuario(
                nombre = "Administrador General",
                email  = "admin@alces.cl",
                rut    = "11.111.111-1",
                rol    = "Admin",
                activo = true,
                fotoPath = null,
                // 👇 password en TEXTO PLANO
                password = "admin123"
            )
            dao.insertar(admin)
        }
    }
}



