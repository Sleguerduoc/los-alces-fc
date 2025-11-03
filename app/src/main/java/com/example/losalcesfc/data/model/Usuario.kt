package com.example.losalcesfc.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val rut: String,
    val rol: String,
    val activo: Boolean = true,
    val fotoPath: String? = null,
    val password: String
)
