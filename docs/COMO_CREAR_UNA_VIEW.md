# 📱 Cómo Crear una Ventana View de Manera Simple

Esta guía te enseñará a crear una nueva pantalla (view) en la aplicación Foro de Cine de forma sencilla y siguiendo las mejores prácticas de Jetpack Compose.

## 📋 Tabla de Contenidos

1. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
2. [Patrón de Diseño](#patrón-de-diseño)
3. [Pasos para Crear una Nueva View](#pasos-para-crear-una-nueva-view)
4. [Ejemplo Completo](#ejemplo-completo)
5. [Integración con la Navegación](#integración-con-la-navegación)
6. [Mejores Prácticas](#mejores-prácticas)

---

## 🏗️ Arquitectura del Proyecto

El proyecto utiliza la arquitectura **MVVM (Model-View-ViewModel)** con **Jetpack Compose**:

```
app/src/main/java/com/proyectoforocine/
├── view/              # 👁️ Pantallas UI (Composables)
├── viewmodel/         # 🧠 Lógica de negocio y estado
├── model/             # 📦 Modelos de datos UI
├── data/              # 💾 Base de datos Room
└── repository/        # 🔄 Capa de datos
```

---

## 🎯 Patrón de Diseño

Las pantallas en esta aplicación son **"tontas" (stateless)**, lo que significa que:

- ✅ **NO** contienen lógica de negocio
- ✅ **NO** manejan ViewModels directamente
- ✅ **SÍ** reciben el estado como parámetros
- ✅ **SÍ** notifican eventos mediante callbacks (lambdas)

**Ventajas:**
- 📝 Fácil de testear
- 🔄 Reutilizable
- 👀 Código más limpio y legible
- 🎨 Fácil de previsualizar con `@Preview`

---

## 🚀 Pasos para Crear una Nueva View

### Paso 1: Crear el Archivo de la Pantalla

Crea un nuevo archivo en `/app/src/main/java/com/proyectoforocine/view/`:

```kotlin
// Ejemplo: MiNuevaPantallaScreen.kt
package com.proyectoforocine.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
```

### Paso 2: Definir el Composable Principal

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiNuevaPantallaScreen(
    // Parámetros de estado
    titulo: String,
    descripcion: String,
    // Callbacks de eventos
    onButtonClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Nueva Pantalla") },
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Acción Principal")
            }
        }
    }
}
```

### Paso 3: Agregar Preview para Desarrollo

```kotlin
@Preview(showBackground = true, name = "Mi Nueva Pantalla")
@Composable
fun MiNuevaPantallaScreenPreview() {
    ProyectoForoCineTheme {
        MiNuevaPantallaScreen(
            titulo = "Título de Ejemplo",
            descripcion = "Esta es una descripción de ejemplo para la preview",
            onButtonClick = {},
            onNavigateBack = {}
        )
    }
}
```

---

## 💡 Ejemplo Completo

Aquí tienes un ejemplo completo de una pantalla simple basada en el patrón del proyecto:

```kotlin
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
 * Pantalla de ejemplo que muestra cómo crear una view simple.
 * Es "tonta" (stateless) - no contiene lógica de ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EjemploSimpleScreen(
    titulo: String,
    contenido: String,
    onTituloChange: (String) -> Unit,
    onContenidoChange: (String) -> Unit,
    onGuardarClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ejemplo Simple") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de texto para el título
            OutlinedTextField(
                value = titulo,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Campo de texto para el contenido
            OutlinedTextField(
                value = contenido,
                onValueChange = onContenidoChange,
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Botón de acción
            Button(
                onClick = onGuardarClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EjemploSimpleScreenPreview() {
    ProyectoForoCineTheme {
        EjemploSimpleScreen(
            titulo = "Mi Título",
            contenido = "Este es el contenido de ejemplo",
            onTituloChange = {},
            onContenidoChange = {},
            onGuardarClick = {},
            onNavigateBack = {}
        )
    }
}
```

---

## 🗺️ Integración con la Navegación

Para que tu nueva pantalla sea accesible, debes agregarla al `NavHost` en `MainActivity.kt`:

### Paso 1: Importar tu Pantalla

```kotlin
import com.proyectoforocine.view.MiNuevaPantallaScreen
```

### Paso 2: Agregar la Ruta en el NavHost

```kotlin
NavHost(
    navController = navController,
    startDestination = startDestination
) {
    // ... otras rutas existentes ...
    
    // Tu nueva ruta
    composable("mi_nueva_pantalla") {
        MiNuevaPantallaScreen(
            titulo = "Título desde ViewModel",
            descripcion = "Descripción desde ViewModel",
            onButtonClick = {
                // Lógica del botón
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
```

### Paso 3: Navegar a tu Pantalla

Desde cualquier otra pantalla, usa:

```kotlin
navController.navigate("mi_nueva_pantalla")
```

---

## ✨ Mejores Prácticas

### 1. **Usa Scaffold para la Estructura Base**

```kotlin
Scaffold(
    topBar = { /* TopAppBar */ },
    floatingActionButton = { /* FAB opcional */ },
    bottomBar = { /* BottomBar opcional */ }
) { paddingValues ->
    // Contenido principal
}
```

### 2. **Mantén la Pantalla Stateless**

❌ **No hagas esto:**
```kotlin
@Composable
fun MiPantalla() {
    val viewModel: MiViewModel = viewModel()  // ❌ NO
    val estado = viewModel.estado.collectAsState()
}
```

✅ **Haz esto:**
```kotlin
@Composable
fun MiPantalla(
    estado: Estado,                    // ✅ SÍ - Recibe estado
    onAction: () -> Unit              // ✅ SÍ - Callbacks para eventos
)
```

### 3. **Usa Material3 Components**

El proyecto usa **Material Design 3**. Componentes comunes:

- `Button` - Botones de acción
- `OutlinedButton` - Botones secundarios
- `OutlinedTextField` - Campos de texto
- `Card` - Tarjetas de contenido
- `IconButton` - Botones con iconos
- `FloatingActionButton` - FAB para acciones principales

### 4. **Organiza el Layout con Modifier**

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()              // Ocupa todo el espacio
        .padding(paddingValues)     // Respeta SafeArea
        .padding(16.dp),            // Padding interno
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    // Contenido
}
```

### 5. **Agrega testTag para Testing**

```kotlin
Button(
    onClick = onGuardarClick,
    modifier = Modifier
        .fillMaxWidth()
        .testTag("guardarButton")  // Para tests instrumentados
) {
    Text("Guardar")
}
```

### 6. **Crea Previews con Diferentes Estados**

```kotlin
@Preview(showBackground = true, name = "Estado Normal")
@Composable
fun NormalPreview() {
    ProyectoForoCineTheme {
        MiPantalla(/* estado normal */)
    }
}

@Preview(showBackground = true, name = "Con Error")
@Composable
fun ErrorPreview() {
    ProyectoForoCineTheme {
        MiPantalla(/* estado con error */)
    }
}

@Preview(showBackground = true, name = "Modo Oscuro", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkModePreview() {
    ProyectoForoCineTheme(darkTheme = true) {
        MiPantalla(/* estado normal */)
    }
}
```

---

## 🎨 Componentes Comunes del Proyecto

### TopAppBar con Navegación

```kotlin
TopAppBar(
    title = { Text("Mi Pantalla") },
    navigationIcon = {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
    },
    actions = {
        IconButton(onClick = onAction) {
            Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
        }
    }
)
```

### Card para Contenido

```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onCardClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Título", style = MaterialTheme.typography.titleMedium)
        Text(text = "Descripción", style = MaterialTheme.typography.bodySmall)
    }
}
```

### FloatingActionButton

```kotlin
floatingActionButton = {
    FloatingActionButton(onClick = onFabClick) {
        Icon(Icons.Default.Add, contentDescription = "Agregar")
    }
}
```

---

## 📚 Recursos Adicionales

### Documentación Oficial

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

### Ejemplos en el Proyecto

Estudia estas pantallas existentes para más ejemplos:

- `LoginScreen.kt` - Formulario simple con validación
- `CrearTemaScreen.kt` - Formulario con múltiples campos
- `ListaTemasScreen.kt` - Lista con FAB y TopAppBar
- `PerfilScreen.kt` - Perfil de usuario con imagen y configuración

---

## 🎯 Resumen Rápido

Para crear una nueva view:

1. ✅ Crea un archivo `.kt` en `view/`
2. ✅ Define un `@Composable` con parámetros (estado + callbacks)
3. ✅ Usa `Scaffold` como estructura base
4. ✅ Agrega un `@Preview` para desarrollo
5. ✅ Integra la pantalla en `MainActivity.kt` (NavHost)
6. ✅ Mantén la pantalla stateless (sin ViewModel directo)

**¡Y listo! Ya tienes tu nueva pantalla funcionando.** 🚀

---

**Última actualización:** Diciembre 2024  
**Versión del Proyecto:** 1.0.0
