package com.proyectoforocine.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.proyectoforocine.data.local.Tema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisTemasScreen(
    temas: List<Tema>,
    onNavigateBack: () -> Unit,
    onTemaClick: (Tema) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Temas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (temas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no has creado ningún tema.")
            }
        } else {
            // Reutilizamos la misma lista de temas que en la pantalla principal
            ListaTemasScreenContent(temas = temas, onTemaClick = onTemaClick, contentPadding = paddingValues)
        }
    }
}