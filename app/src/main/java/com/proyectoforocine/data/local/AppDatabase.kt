package com.proyectoforocine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tema::class, UsuarioEntity::class], version = 4, exportSchema = false) // Versión incrementada a 4
abstract class AppDatabase : RoomDatabase() {

    abstract fun temaDao(): TemaDao

    abstract fun usuarioDao(): UsuarioDao

}
