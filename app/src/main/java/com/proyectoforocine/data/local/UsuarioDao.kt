package com.proyectoforocine.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarUsuario(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM Usuario WHERE email = :email LIMIT 1")
    suspend fun getUsuarioPorEmail(email: String): UsuarioEntity?

    /**
     * Busca un usuario por su ID y devuelve un Flow.
     * Esto permite que la UI reaccione a cambios en el perfil del usuario.
     */
    @Query("SELECT * FROM Usuario WHERE id = :id LIMIT 1")
    fun getUsuarioPorId(id: Long): Flow<UsuarioEntity?>
}
