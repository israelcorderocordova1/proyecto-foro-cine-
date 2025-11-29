package com.proyectoforocine

import android.app.Application
import androidx.room.Room
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.datastore.SessionManager
import com.proyectoforocine.data.local.AppDatabase

class ForoApplication : Application() {

    // Instancia de DataStore para la sesión
    val sessionManager: SessionManager by lazy {
        SessionManager(this)
    }

    // Usamos 'lazy' para que la base de datos y el repositorio
    // solo se creen cuando se necesiten por primera vez.
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foro-db"
        )
        .fallbackToDestructiveMigration() // ¡Importante para desarrollo!
        .build()
    }

    // El repositorio se crea usando los DAOs de la instancia de la base de datos.
    val repository: ForoRepository by lazy {
        ForoRepository(database.temaDao(), database.usuarioDao())
    }
}