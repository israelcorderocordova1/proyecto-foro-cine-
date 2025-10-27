package com.proyectoforocine.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.proyectoforocine.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para acceder fácilmente al DataStore desde cualquier Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    // Claves para guardar y leer datos del usuario
    companion object {
        val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_PHOTO_URI_KEY = stringPreferencesKey("user_photo_uri")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
    }

    // Flujo que emite el rol del usuario cada vez que cambia.
    val userRole: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ROLE_KEY]
    }

    // Flujo que emite el perfil completo del usuario
    val userProfile: Flow<UserProfile> = context.dataStore.data.map { preferences ->
        UserProfile(
            nombre = preferences[USER_NAME_KEY] ?: "Usuario",
            fotoUri = preferences[USER_PHOTO_URI_KEY],
            modoOscuro = preferences[DARK_MODE_KEY] ?: false,
            recibirNotificaciones = preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
        )
    }

    // Función para guardar el rol del usuario
    suspend fun saveUserRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role
        }
    }

    // Función para guardar el nombre del usuario
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // Función para guardar la URI de la foto de perfil
    suspend fun saveUserPhoto(photoUri: String?) {
        context.dataStore.edit { preferences ->
            if (photoUri != null) {
                preferences[USER_PHOTO_URI_KEY] = photoUri
            } else {
                preferences.remove(USER_PHOTO_URI_KEY)
            }
        }
    }

    // Función para guardar la preferencia de modo oscuro
    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    // Función para guardar la preferencia de notificaciones
    suspend fun saveNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    // Función para guardar el perfil completo
    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = profile.nombre
            if (profile.fotoUri != null) {
                preferences[USER_PHOTO_URI_KEY] = profile.fotoUri
            } else {
                preferences.remove(USER_PHOTO_URI_KEY)
            }
            preferences[DARK_MODE_KEY] = profile.modoOscuro
            preferences[NOTIFICATIONS_ENABLED_KEY] = profile.recibirNotificaciones
        }
    }
}