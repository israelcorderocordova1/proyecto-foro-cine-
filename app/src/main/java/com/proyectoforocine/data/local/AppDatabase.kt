package com.proyectoforocine.data.local



import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * La clase principal de la base de datos de Room.
 *
 * @Database: Anotación que define la configuración de la base de datos.
 *  - entities: Array de todas las clases de entidad (tablas) que pertenecen a esta base de datos.
 *  - version: Número de versión de la base de datos. Debe incrementarse cuando cambias el esquema.
 *  - exportSchema: Es recomendable mantenerlo en 'false' para proyectos simples para evitar un warning.
 */
@Database(entities = [Tema::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Declara una función abstracta que devuelve el DAO para la entidad 'Tema'.
     * Room generará la implementación de este método por nosotros.
     */
    abstract fun temaDao(): TemaDao

}
