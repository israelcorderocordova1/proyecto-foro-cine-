package com.proyectoforocine.data.local





import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa la tabla 'Tema' en la base de datos.
 *
 * --- ANOTACIONES CLAVE ---
 * @Entity(tableName = "Tema"): Marca esta clase como una tabla de la base de datos.
 *   - Por defecto, Room usa el nombre de la clase como nombre de la tabla.
 *
 * @PrimaryKey(autoGenerate = true): Marca el campo 'id' como la clave primaria.
 *   - 'autoGenerate = true' le dice a Room que genere automáticamente un ID único
 *     para cada nuevo tema que se inserte, asegurando que nunca haya duplicados.
 */
@Entity(tableName = "Tema")
data class Tema(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // El ID debe tener un valor por defecto para que Room lo autogenere.

    val titulo: String,
    val contenido: String
)
