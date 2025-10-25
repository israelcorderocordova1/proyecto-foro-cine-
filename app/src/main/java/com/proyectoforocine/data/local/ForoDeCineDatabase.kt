package com.proyectoforocine.data.local



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Tema::class], version = 1, exportSchema = false)
abstract class ForoDeCineDatabase : RoomDatabase() {

    abstract fun temaDao(): TemaDao

    companion object {
        @Volatile
        private var INSTANCE: ForoDeCineDatabase? = null

        fun getDatabase(context: Context): ForoDeCineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForoDeCineDatabase::class.java,
                    "foro_cine_database" // Nombre del archivo de la base de datos
                )
                    .fallbackToDestructiveMigration() // Ojo: borra la BD si se cambia la versión
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}