package com.proyectoforocine



import android.app.Application
import androidx.room.Room
import com.proyectoforocine.data.ForoRepository
import com.proyectoforocine.data.local.AppDatabase

class ForoApplication : Application() {

    // Usamos 'lazy' para que la base de datos y el repositorio
    // solo se creen cuando se necesiten por primera vez.
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foro-db" // Nombre del archivo de la base de datos
        ).build()
    }

    // El repositorio se crea usando el DAO de la instancia de la base de datos.
    val repository: ForoRepository by lazy {
        ForoRepository(database.temaDao()) // Asumiendo que tu AppDatabase tiene un método temaDao()
    }
}
