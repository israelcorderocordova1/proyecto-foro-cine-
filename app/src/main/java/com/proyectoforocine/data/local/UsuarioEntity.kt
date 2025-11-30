package com.proyectoforocine.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Usuario",
    indices = [Index(value = ["email"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val username: String,
    val email: String,
    val password: String, // ¡Recuerda que esto debería ser un hash!
    val rol: String,
    val registrationDate: Long // Nuevo campo para la fecha de registro (timestamp)
)
