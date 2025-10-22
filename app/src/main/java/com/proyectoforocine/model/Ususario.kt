package com.proyectoforocine.model

data class Usuario(
    val id: String,
    val nombre: String,
    val rol: String // "registrado" o "moderador"
)