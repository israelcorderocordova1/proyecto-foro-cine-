package com.proyectoforocine.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.model.TemaForo
import com.proyectoforocine.model.Usuario
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTemaScreen(
    tema: TemaForo?,
    rol: String?,
    onNavigateBack: () -> Unit,
    onDeleteTema: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tema?.titulo ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        tema?.let {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(it.titulo, style = MaterialTheme.typography.headlineSmall)
                Text("por ${it.autor.nombre}", style = MaterialTheme.typography.bodyMedium)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(it.contenido, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

                if (rol == "moderador") {
                    Button(
                        onClick = onDeleteTema,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Eliminar Tema (Moderador)")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Muestra un indicador de carga
        }
    }
}

// --- Previews ---

val sampleTemaDetalle = TemaForo(
    id = 1L,
    titulo = "Título del Tema en Detalle",
    contenido = "Este es el contenido completo del tema. A diferencia de la tarjeta, aquí se muestra todo el texto, sin importar qué tan largo sea. Esto permite a los usuarios leer toda la discusión y los puntos presentados por el autor original.",
    autor = Usuario(id = "1", nombre = "Autor Detallado", rol = "registrado"),
    categoria = "Discusión General"
)

@Preview(showBackground = true, name = "Detalle Tema - Usuario")
@Composable
fun DetalleTemaScreenUserPreview() {
    ProyectoForoCineTheme {
        DetalleTemaScreen(
            tema = sampleTemaDetalle,
            rol = "registrado",
            onNavigateBack = {},
            onDeleteTema = {}
        )
    }
}

@Preview(showBackground = true, name = "Detalle Tema - Moderador")
@Composable
fun DetalleTemaScreenModeradorPreview() {
    ProyectoForoCineTheme {
        DetalleTemaScreen(
            tema = sampleTemaDetalle,
            rol = "moderador",
            onNavigateBack = {},
            onDeleteTema = {}
        )
    }
}

@Preview(showBackground = true, name = "Detalle Tema - Cargando")
@Composable
fun DetalleTemaScreenLoadingPreview() {
    ProyectoForoCineTheme {
        DetalleTemaScreen(
            tema = null, // Estado de carga
            rol = "registrado",
            onNavigateBack = {},
            onDeleteTema = {}
        )
    }
}
