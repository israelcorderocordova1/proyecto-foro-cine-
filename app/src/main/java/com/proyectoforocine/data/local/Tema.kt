package com.proyectoforocine.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Tema",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE // Si se borra un usuario, sus temas se borran
        )
    ]
)
data class Tema(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val titulo: String,
    val contenido: String,
    val authorId: Long // Nuevo campo para el ID del autor
)
