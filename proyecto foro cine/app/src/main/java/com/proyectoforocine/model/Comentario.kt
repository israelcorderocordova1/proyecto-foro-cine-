package com.proyectoforocine.model

data class Comentario(
    val id: Int,
    val autor: Usuario,
    val texto: String
)