package com.proyectoforocine

// C:/Users/Israel/Desktop/proyecto foro cine/app/src/main/java/com/proyectoforocine/ForoDeCineApplication.kt



import android.app.Application
import com.proyectoforocine.data.local.ForoDeCineDatabase
import com.proyectoforocine.data.local.TemaRepository

class ForoDeCineApplication : Application() {
    // Usando 'lazy' para que la base de datos y el repositorio
    // solo se creen cuando se necesiten por primera vez.
    val database by lazy { ForoDeCineDatabase.getDatabase(this) }
    val repository by lazy { TemaRepository(database.temaDao()) }
}