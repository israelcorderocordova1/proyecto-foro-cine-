package com.proyectoforocine.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.model.categoriasPeliculas
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarTemaScreen(
    onNavigateBack: () -> Unit,
    onTemaPublicado: (titulo: String, contenido: String, categoria: String) -> Unit
) {

    // 1. La pantalla maneja su propio estado
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(categoriasPeliculas[0]) }

    // 2. La pantalla maneja sus propios errores
    var errorTitulo by remember { mutableStateOf<String?>(null) }
    var errorContenido by remember { mutableStateOf<String?>(null) }

    // 3. La pantalla tiene su propia validación
    fun validarFormularioYPublicar() {
        val tieneErrorTitulo = titulo.isBlank()
        val tieneErrorContenido = contenido.isBlank()

        errorTitulo = if (tieneErrorTitulo) "El título no puede estar vacío" else null
        errorContenido = if (tieneErrorContenido) "El contenido no puede estar vacío" else null

        // Si no hay errores, publica
        if (!tieneErrorTitulo && !tieneErrorContenido) {
            onTemaPublicado(titulo, contenido, categoria)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Nuevo Tema") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxSize()
        ) {
            // --- Campo Título ---
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    errorTitulo = null // Borra el error al escribir
                },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorTitulo != null,
                singleLine = true
            )
            if (errorTitulo != null) {
                Text(errorTitulo!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo Contenido ---
            OutlinedTextField(
                value = contenido,
                onValueChange = {
                    contenido = it
                    errorContenido = null // Borra el error al escribir
                },
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                isError = errorContenido != null
            )
            if (errorContenido != null) {
                Text(errorContenido!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Selector de Categoría ---
            CategoriaDropDown(
                categoriaSeleccionada = categoria,
                onCategoriaChange = { categoria = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Botón de Publicación ---
            Button(
                onClick = { validarFormularioYPublicar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar Tema")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaDropDown(
    categoriaSeleccionada: String,
    onCategoriaChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = categoriaSeleccionada,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoría") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoriasPeliculas.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria) },
                    onClick = {
                        onCategoriaChange(categoria)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PublicarTemaScreenPreview() {
    ProyectoForoCineTheme {
        PublicarTemaScreen(
            onNavigateBack = {},
            onTemaPublicado = { _, _, _ -> }
        )
    }
}