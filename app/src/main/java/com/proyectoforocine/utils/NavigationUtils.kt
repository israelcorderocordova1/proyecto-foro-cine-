package com.proyectoforocine.utils

/**
 * Resuelve el destino inicial de navegación según un flag.
 * Esto permite cubrir lógica de MainActivity con tests JUnit puros.
 */
fun resolverDestinoInicial(iniciarEnLista: Boolean): String {
    return if (iniciarEnLista) "lista_temas" else "login"
}
