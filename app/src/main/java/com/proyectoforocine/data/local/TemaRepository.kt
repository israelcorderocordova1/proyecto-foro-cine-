package com.proyectoforocine.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que maneja los datos de los temas.
 * Es la única fuente de verdad para los ViewModels.
 * Recibe el DAO como dependencia.
 */
class TemaRepository(private val temaDao: TemaDao) {

    // Expone el Flow de la base de datos para que el ViewModel lo observe.
    val todosLosTemas: Flow<List<Tema>> = temaDao.obtenerTodosLosTemas()

    // Función 'suspend' para insertar un tema. Se llamará desde una corrutina.
    suspend fun insertarTema(tema: Tema) {
        temaDao.insertarTema(tema)
    }

    // Función 'suspend' para eliminar un tema.
    suspend fun eliminarTema(tema: Tema) {
        temaDao.eliminarTema(tema)
    }
}