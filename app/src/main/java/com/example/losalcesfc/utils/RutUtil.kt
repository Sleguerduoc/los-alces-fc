package com.example.losalcesfc.utils

object RutUtil {
    fun canon(rut: String): String =
        rut.replace(".", "").replace("-", "").trim().uppercase()
}
