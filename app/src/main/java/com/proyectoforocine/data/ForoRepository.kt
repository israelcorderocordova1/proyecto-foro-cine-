package com.proyectoforocine.data

import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.TemaDao
import com.proyectoforocine.data.local.UsuarioEntity
import com.proyectoforocine.data.local.UsuarioDao
import kotlinx.coroutines.flow.Flow

class ForoRepository(private val temaDao: TemaDao, private val usuarioDao: UsuarioDao) {

    // --- Métodos para Temas ---

    val todosLosTemas: Flow<List<Tema>> = temaDao.obtenerTodosLosTemas()

    fun obtenerTemaPorId(id: Long): Flow<Tema?> {
        return temaDao.obtenerTemaPorId(id)
    }

    fun getTemasPorAutorId(authorId: Long): Flow<List<Tema>> {
        return temaDao.getTemasPorAutorId(authorId)
    }

    suspend fun insertarTema(tema: Tema) {
        temaDao.insertarTema(tema)
    }

    suspend fun eliminarTema(tema: Tema) {
        temaDao.eliminarTema(tema)
    }

    // --- Métodos para Usuarios ---

    suspend fun insertarUsuario(usuario: UsuarioEntity): Long {
        return usuarioDao.insertarUsuario(usuario)
    }

    suspend fun getUsuarioPorEmail(email: String): UsuarioEntity? {
        return usuarioDao.getUsuarioPorEmail(email)
    }

    fun getUsuarioPorId(id: Long): Flow<UsuarioEntity?> {
        return usuarioDao.getUsuarioPorId(id)
    }
}
