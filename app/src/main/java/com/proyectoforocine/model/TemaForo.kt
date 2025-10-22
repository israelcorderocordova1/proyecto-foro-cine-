package com.proyectoforocine.model

data class TemaForo(
    val id: Long, // Cambiado a Long para que coincida con la navegación
    val titulo: String,
    val contenido: String,
    val autor: Usuario,
    val categoria: String, // "Noticias", "Preguntas", etc.
    var valoracion: Int = 0,
    val comentarios: MutableList<Comentario> = mutableListOf()
)