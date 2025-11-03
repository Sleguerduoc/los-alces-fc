package com.example.losalcesfc.utils

object RutUtil {
    /** Retorna RUT canónico sin puntos/guión y con DV en mayúscula. Ej: "12.345.678-5" -> "123456785" */
    fun canon(rut: String): String =
        rut.replace(".", "").replace("-", "").trim().uppercase()
}
