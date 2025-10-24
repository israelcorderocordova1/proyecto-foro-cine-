package com.proyectoforocine.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.model.categoriasPeliculas
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicarTemaScreen(
    onTemaPublicado: (titulo: String, contenido: String, categoria: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    // --- Gestión del Estado del Formulario ---
    var titulo by remember { mutableStateOf("") } // Sirve para almacenar el texto del título.
    var contenido by remember { mutableStateOf("") } // Sirve para almacenar el texto del contenido.
    var categoriaSeleccionada by remember { mutableStateOf(categoriasPeliculas[0]) } // Sirve para la categoría. Se inicia con el primer elemento de la lista.

    // --- Gestión del Estado de Validación ---
    var isTituloError by remember { mutableStateOf(false) } // Sirbe para saber si hay un error en el campo título.
    var isContenidoError by remember { mutableStateOf(false) } // Sirve para saber si hay un error en el campo contenido.

    // --- Lógica de Negocio ---
    fun validarFormulario(): Boolean {
        isTituloError = titulo.isBlank() // Si el título está vacío o solo tiene espacios en blanco, marca error.
        isContenidoError = contenido.isBlank() // Si el contenido está vacío, marca error.
        return !isTituloError && !isContenidoError // Devuelve true solo si no hay errores.
    }

    // --- Estructura de la Interfaz de Usuario ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Nuevo Tema") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // --- Campo de Texto para el Título ---
            OutlinedTextField(
                value = titulo,
                onValueChange = { // Se ejecuta cada vez que el usuario escribe algo.
                    titulo = it
                    isTituloError = false // Limpia el error en cuanto el usuario empieza a escribir.
                },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                isError = isTituloError,
                singleLine = true
            )
            // Si hay un error en el título, muestra un mensaje de ayuda.
            if (isTituloError) {
                Text("El título no puede estar vacío", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = contenido,
                onValueChange = {
                    contenido = it
                    isContenidoError = false
                },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                isError = isContenidoError
            )
            // Si hay un error en el contenido, muestra un mensaje de ayuda.
            if (isContenidoError) {
                Text("El contenido no puede estar vacío", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Selector de Categoría ---
            CategoriaDropDown(
                categoriaSeleccionada = categoriaSeleccionada,
                onCategoriaChange = { categoriaSeleccionada = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validarFormulario()) {
                        onTemaPublicado(titulo, contenido, categoriaSeleccionada)
                    }
                },
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
            onTemaPublicado = { _, _, _ ->
                println("Vista previa: Tema publicado")
            },
            onNavigateBack = {
                println("Vista previa: Navegar hacia atrás")
            }
        )
    }
}
