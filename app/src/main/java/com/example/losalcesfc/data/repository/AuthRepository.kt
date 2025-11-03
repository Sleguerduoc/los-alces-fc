package com.example.losalcesfc.data.repository

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import com.example.losalcesfc.data.local.SessionPrefs

class AuthRepository private constructor(private val prefs: SessionPrefs) {

    companion object {
        private var INSTANCE: AuthRepository? = null

        fun init(context: Context) {
            INSTANCE = AuthRepository(SessionPrefs.get(context))
        }

        val instance: AuthRepository
            get() = requireNotNull(INSTANCE) { "AuthRepository.init(context) no fue llamado" }
    }

    // Credenciales demo
    private val demoUser = "admin@alces.cl"
    private val demoPass = "alces123"

    suspend fun login(user: String, pass: String): Result<Unit> {
        delay(600) // simula red
        return if (user == demoUser && pass == demoPass) {
            prefs.setLoggedIn(user)
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    fun isLoggedIn(): Flow<Boolean> = prefs.isLoggedIn

    suspend fun logout() {
        prefs.clear()
    }

    suspend fun requestPasswordReset(email: String): Result<Unit> {
        delay(800)
        return if (email.contains("@") && email.contains(".")) Result.success(Unit)
        else Result.failure(IllegalArgumentException("Correo inválido"))
    }

}
