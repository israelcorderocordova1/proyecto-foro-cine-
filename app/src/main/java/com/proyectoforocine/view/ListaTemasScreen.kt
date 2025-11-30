package com.proyectoforocine.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.data.local.Tema
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTemasScreen(
    temas: List<Tema>,
    onTemaClick: (Tema) -> Unit,
    onAddTemaClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onLogoutClick: () -> Unit // Nuevo parámetro para cerrar sesión
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foro de Cine") },
                actions = {
                    IconButton(onClick = onPerfilClick) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Mi Perfil")
                    }
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTemaClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear nuevo tema")
            }
        }
    ) { paddingValues ->
        ListaTemasScreenContent(temas = temas, onTemaClick = onTemaClick, contentPadding = paddingValues)
    }
}

@Composable
fun ListaTemasScreenContent(
    temas: List<Tema>,
    onTemaClick: (Tema) -> Unit,
    contentPadding: PaddingValues
) {
    if (temas.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(contentPadding).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no hay temas. ¡Sé el primero en crear uno!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(temas) { tema ->
                TemaItem(
                    tema = tema,
                    onItemClick = { onTemaClick(tema) }
                )
            }
        }
    }
}

@Composable
fun TemaItem(
    tema: Tema,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.titulo, style = MaterialTheme.typography.titleLarge)
            if (tema.contenido.isNotBlank()) {
                Text(
                    text = tema.contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTemasScreenPreview() {
    ProyectoForoCineTheme {
        val sampleTemas = listOf(
            Tema(id = 1, titulo = "Review de Dune 2", contenido = "Una obra maestra de la ciencia ficción...", authorId = 1L),
            Tema(id = 2, titulo = "Peliculas sobrevaloradas?", contenido = "Abro debate: El Padrino está sobrevalorada.", authorId = 1L)
        )
        ListaTemasScreen(
            temas = sampleTemas, 
            onTemaClick = {}, 
            onAddTemaClick = {}, 
            onPerfilClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Lista Vacía")
@Composable
fun ListaTemasScreenEmptyPreview() {
    ProyectoForoCineTheme {
        ListaTemasScreen(temas = emptyList(), onTemaClick = {}, onAddTemaClick = {}, onPerfilClick = {}, onLogoutClick = {})
    }
}
