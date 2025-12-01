package com.proyectoforocine.data



import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.data.local.TemaDao
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que gestiona los datos de los temas del foro.
 * Es la única fuente de verdad para los ViewModels.
 * Decide de dónde obtener los datos (en este caso, de la base de datos local de Room).
 */
class ForoRepository(private val temaDao: TemaDao) {

    // Expone el Flow de todos los temas directamente desde el DAO.
    // Cualquier cambio en la tabla 'temas' se reflejará automáticamente aquí.
    val todosLosTemas: Flow<List<Tema>> = temaDao.obtenerTodosLosTemas()

    // Función para obtener un tema específico por su ID.
    fun obtenerTemaPorId(id: Long): Flow<Tema?> {
        return temaDao.obtenerTemaPorId(id)
    }

    // Función 'suspend' para insertar un nuevo tema. Debe ser llamada desde una corrutina.
    suspend fun insertarTema(tema: Tema) {
        temaDao.insertarTema(tema)
    }

    // Función 'suspend' para eliminar un tema.
    suspend fun eliminarTema(tema: Tema) {
        temaDao.eliminarTema(tema)
    }
}
