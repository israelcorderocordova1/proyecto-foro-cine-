package com.proyectoforocine.view

// Asegúrate de que este import sea el correcto
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.viewmodel.CrearTemaUiState

/**
 * Pantalla para crear un nuevo tema. Es "tonta" (stateless).
 * No contiene lógica de ViewModel. Recibe el estado (uiState)
 * y los eventos (lambdas) desde un Composable superior (MainActivity).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearTemaScreen(
    uiState: CrearTemaUiState,
    onTituloChange: (String) -> Unit,
    onContenidoChange: (String) -> Unit,
    onPublicarClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crea un nuevo Tema") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = onTituloChange,
                label = { Text("Agrega un Título") },
                modifier = Modifier.fillMaxWidth(),
                // --- CORRECCIÓN AQUÍ ---
                isError = uiState.error != null,
                singleLine = true
            )
            // --- CORRECCIÓN AQUÍ ---
            if (uiState.error != null) {
                Text(
                    // --- CORRECCIÓN AQUÍ ---
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.contenido,
                onValueChange = onContenidoChange,
                label = { Text("Cuentanos sobre el tema que escogiste") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onPublicarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Compartelo al Foro")
            }
        }
    }
}


@Preview(showBackground = true, name = "Crear Tema - Normal")
@Composable
fun CrearTemaScreenPreview() {
    ProyectoForoCineTheme {
        CrearTemaScreen(
            uiState = CrearTemaUiState(titulo = "Mi Título", contenido = "Este es mi contenido..."),
            onTituloChange = {},
            onContenidoChange = {},
            onPublicarClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Crear Tema - Con Error")
@Composable
fun CrearTemaScreenErrorPreview() {
    ProyectoForoCineTheme {
        CrearTemaScreen(
            // --- CORRECCIÓN AQUÍ ---
            uiState = CrearTemaUiState(error = "El título no puede estar vacío"),
            onTituloChange = {},
            onContenidoChange = {},
            onPublicarClick = {},
            onNavigateBack = {}
        )
    }
}
