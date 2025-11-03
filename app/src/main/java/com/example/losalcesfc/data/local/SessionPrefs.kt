package com.example.losalcesfc.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "session_prefs")

class SessionPrefs private constructor(private val context: Context) {

    companion object {
        private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
        private val KEY_EMAIL     = stringPreferencesKey("email")

        @Volatile private var INSTANCE: SessionPrefs? = null
        fun get(context: Context): SessionPrefs =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionPrefs(context.applicationContext).also { INSTANCE = it }
            }
    }

    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(email: String) {
        context.dataStore.edit {
            it[KEY_LOGGED_IN] = true
            it[KEY_EMAIL] = email
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
