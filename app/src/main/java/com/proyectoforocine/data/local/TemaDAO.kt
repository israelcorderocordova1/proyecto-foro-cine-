package com.proyectoforocine.data.local

// --- CORRECCIÓN CLAVE ---
// La ruta de importación debe apuntar a donde realmente está tu clase Tema.
// Como está en el mismo paquete ('data.local'), no necesitamos un import específico,
// pero por claridad, vamos a corregir el que teníamos.
// Si tu archivo Tema.kt está en 'data/local', el import sería:
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemaDao {

    // El nombre de la tabla por defecto es el nombre de la clase, 'Tema'.
    @Query("SELECT * FROM Tema ORDER BY id DESC")
    fun obtenerTodosLosTemas(): Flow<List<Tema>>

    @Query("SELECT * FROM Tema WHERE id = :id")
    fun obtenerTemaPorId(id: Long): Flow<Tema?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTema(tema: Tema)

    @Delete
    suspend fun eliminarTema(tema: Tema)
}

