package com.proyectoforocine.repository

import androidx.compose.runtime.mutableStateListOf
import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario

// Usamos un 'object' para crear un Singleton: una única instancia del repositorio para toda la app.
object ForoRepository {

    // Usuario de ejemplo que usaremos para crear temas
    private val usuarioEjemplo = Usuario(id = "1", nombre = "CinéfiloExperto", rol = "registrado")
    private var nextId = 3L // Empezamos desde 3, ya que tenemos dos temas iniciales

    // Esta lista mutable y observable actuará como nuestra base de datos en memoria.
    private val _temas = mutableStateListOf<TemaForo>(
        // Datos de ejemplo para que la app no inicie vacía
        TemaForo(
            id = 1L,
            titulo = "Opiniones sobre la última película de Marvel",
            contenido = "¿Qué les pareció? A mí me encantó la fotografía, pero el guion dejó que desear.",
            autor = usuarioEjemplo,
            categoria = "General",
            valoracion = 12
        ),
        TemaForo(
            id = 2L,
            titulo = "Películas de terror que recomienden",
            contenido = "Busco algo que de verdad me asuste. ¿Sugerencias?",
            autor = usuarioEjemplo,
            categoria = "Recomendaciones",
            valoracion = 8
        )
    )
    val temas: List<TemaForo> = _temas

    fun getTemaById(id: Long): TemaForo? {
        return _temas.find { it.id == id }
    }

    fun addTema(titulo: String, contenido: String, autor: Usuario) {
        _temas.add(
            0, // Añade el nuevo tema al principio de la lista
            TemaForo(id = nextId++, titulo = titulo, contenido = contenido, autor = autor, categoria = "General")
        )
    }

    fun deleteTema(tema: TemaForo) {
        _temas.remove(tema)
    }
}