package com.proyectoforocine.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Creamos la instancia de DataStore a nivel de archivo, como una extensión de Context.
// Esto garantiza que solo haya una instancia (singleton) de nuestro archivo de preferencias.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

/**
 * Gestiona la sesión del usuario utilizando Jetpack DataStore.
 */
class SessionManager(private val context: Context) {

    // Creamos una clave para almacenar el ID del usuario. Es de tipo Long.
    private val USER_ID_KEY = longPreferencesKey("current_user_id")

    /**
     * Un Flow que emite el ID del usuario actualmente logueado.
     * Si no hay sesión activa, emitirá null.
     * La UI puede observar este Flow para reaccionar a cambios en la sesión.
     */
    val currentUserIdFlow: Flow<Long?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    /**
     * Guarda el ID del usuario en DataStore después de un login/registro exitoso.
     */
    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    /**
     * Limpia el ID del usuario de DataStore al cerrar sesión.
     */
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}