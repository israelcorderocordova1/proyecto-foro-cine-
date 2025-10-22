package com.proyectoforocine.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para acceder fácilmente al DataStore desde cualquier Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    // Clave para guardar y leer el rol del usuario
    companion object {
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }

    // Flujo que emite el rol del usuario cada vez que cambia.
    val userRole: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ROLE_KEY]
    }

    // Función para guardar el rol del usuario
    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
        }
    }
}