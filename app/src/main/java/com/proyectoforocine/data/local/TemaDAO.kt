package com.proyectoforocine.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemaDao {

    @Query("SELECT * FROM Tema ORDER BY id DESC")
    fun obtenerTodosLosTemas(): Flow<List<Tema>>

    @Query("SELECT * FROM Tema WHERE id = :id")
    fun obtenerTemaPorId(id: Long): Flow<Tema?>

    @Query("SELECT * FROM Tema WHERE authorId = :authorId ORDER BY id DESC")
    fun getTemasPorAutorId(authorId: Long): Flow<List<Tema>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTema(tema: Tema)

    @Delete
    suspend fun eliminarTema(tema: Tema)
}
