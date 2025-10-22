package com.proyectoforocine.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTemasScreen(
    temas: List<TemaForo>,
    onTemaClick: (TemaForo) -> Unit,
    onAddTemaClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenido a nuestro Foro de Cine") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTemaClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear Tema")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(8.dp)
        ) {
            items(temas) { tema ->
                TemaCard(tema = tema) {
                    onTemaClick(tema)
                }
            }
        }
    }
}

@Composable
fun TemaCard(tema: TemaForo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = tema.titulo, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "por ${tema.autor.nombre}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tema.contenido.take(100) + "...", // Muestra un preview
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTemasScreenPreview() {
    val sampleTemas = listOf(
        TemaForo(1L, "Título de prueba", "Contenido de prueba...", Usuario("1", "User1", "registrado"), "General"),
        TemaForo(2L, "Otro tema interesante", "Más contenido para ver cómo se ve.", Usuario("2", "User2", "moderador"), "Dudas")
    )
    ProyectoForoCineTheme {
        ListaTemasScreen(temas = sampleTemas, onTemaClick = {}, onAddTemaClick = {})
    }
}


@Preview(showBackground = true)
@Composable
fun TemaCardPreview() {
    val sampleTema = TemaForo(
        id = 1L,
        titulo = "Título de prueba muy largo para ver cómo se ajusta",
        contenido = "Este es el contenido de prueba de un tema del foro. Debería ser lo suficientemente largo como para que se corte con puntos suspensivos.",
        autor = Usuario(id = "1", nombre = "Usuario de Prueba", rol = "registrado"),
        categoria = "General",
        valoracion = 42,
        comentarios = mutableListOf()
    )
    ProyectoForoCineTheme {
        TemaCard(tema = sampleTema, onClick = {})
    }
}