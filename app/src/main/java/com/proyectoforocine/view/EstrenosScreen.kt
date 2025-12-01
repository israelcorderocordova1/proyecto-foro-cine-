package com.proyectoforocine.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.proyectoforocine.model.PeliculaTmdb
import com.proyectoforocine.network.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstrenosScreen(onNavigateBack: () -> Unit) {
    // Estado para las películas
    var peliculas by remember { mutableStateOf<List<PeliculaTmdb>>(emptyList()) }
    val scope = rememberCoroutineScope()

    val apiKey = "428739eef9b0043e952a4c69e78d8084"

    // Cargar datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.tmdbApi.obtenerEstrenos(apiKey)
                peliculas = response.results
            } catch (e: Exception) {
                e.printStackTrace() // Manejo básico de errores
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estrenos en Cartelera") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(peliculas) { peli ->
                Card {
                    Column {
                        AsyncImage(
                            model = peli.getFullPosterUrl(),
                            contentDescription = peli.title,
                            modifier = Modifier.height(180.dp).fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = peli.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}