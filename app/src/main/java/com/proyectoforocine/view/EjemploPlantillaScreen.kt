package com.proyectoforocine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

/**
 * Plantilla de ejemplo para crear una nueva pantalla.
 * 
 * Esta pantalla es "tonta" (stateless), es decir:
 * - NO contiene lógica de ViewModel
 * - Recibe el estado como parámetros
 * - Notifica eventos mediante callbacks (lambdas)
 * 
 * Para usar esta plantilla:
 * 1. Copia este archivo y renómbralo (ej: MiNuevaPantallaScreen.kt)
 * 2. Cambia el nombre de la función y personaliza los parámetros
 * 3. Agrega tu UI personalizada en el contenido del Scaffold
 * 4. Crea un Preview para visualizar tu pantalla
 * 5. Integra la pantalla en MainActivity.kt (NavHost)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjemploPlantillaScreen(
    // Parámetros de estado - personaliza según tus necesidades
    titulo: String,
    contenido: String,
    // Callbacks de eventos - personaliza según tus necesidades
    onTituloChange: (String) -> Unit,
    onContenidoChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Título de la Pantalla") }, // Personaliza el título
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ejemplo: Campo de texto para el título
            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Ejemplo: Campo de texto para el contenido
            OutlinedTextField(
                value = contenido,
                onValueChange = onContenidoChange,
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Ejemplo: Botón de acción principal
            Button(
                onClick = onGuardarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
            
            // Agrega más componentes aquí según necesites:
            // - Text() para mostrar texto
            // - Card() para tarjetas de contenido
            // - LazyColumn() para listas
            // - Image() para imágenes
            // - etc.
        }
    }
}

/**
 * Preview para visualizar la pantalla durante el desarrollo.
 * Puedes crear múltiples previews para diferentes estados.
 */
@Preview(showBackground = true, name = "Estado Normal")
@Composable
fun EjemploPlantillaScreenPreview() {
    ProyectoForoCineTheme {
        EjemploPlantillaScreen(
            titulo = "Mi Título de Ejemplo",
            contenido = "Este es el contenido de ejemplo para la preview",
            onTituloChange = {},
            onContenidoChange = {},
            onGuardarClick = {},
            onNavigateBack = {}
        )
    }
}

/**
 * Preview en modo oscuro
 */
@Preview(showBackground = true, name = "Modo Oscuro")
@Composable
fun EjemploPlantillaScreenDarkPreview() {
    ProyectoForoCineTheme(darkTheme = true) {
        EjemploPlantillaScreen(
            titulo = "Mi Título de Ejemplo",
            contenido = "Este es el contenido de ejemplo en modo oscuro",
            onTituloChange = {},
            onContenidoChange = {},
            onGuardarClick = {},
            onNavigateBack = {}
        )
    }
}
