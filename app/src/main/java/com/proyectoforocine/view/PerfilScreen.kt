package com.proyectoforocine.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.proyectoforocine.model.PerfilUiState
import com.proyectoforocine.model.UserProfile
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    uiState: PerfilUiState,
    onNombreChange: (String) -> Unit,
    onFotoSeleccionada: (Uri?) -> Unit,
    onModoOscuroToggle: (Boolean) -> Unit,
    onNotificacionesToggle: (Boolean) -> Unit,
    onShowImageSourceDialog: () -> Unit,
    onHideImageSourceDialog: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var showPermissionDeniedSnackbar by remember { mutableStateOf(false) }

    // Crear URI temporal para la foto de cámara
    val photoFile = remember {
        File(context.cacheDir, "perfil_temp_${System.currentTimeMillis()}.jpg")
    }
    
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    // Launcher para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onFotoSeleccionada(it) }
        onHideImageSourceDialog()
    }

    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onFotoSeleccionada(photoUri)
        }
        onHideImageSourceDialog()
    }

    // Launcher para permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tempPhotoUri = photoUri
            cameraLauncher.launch(photoUri)
        } else {
            showPermissionDeniedSnackbar = true
            onHideImageSourceDialog()
        }
    }

    // Launcher para permisos de galería (Android 13+)
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            showPermissionDeniedSnackbar = true
            onHideImageSourceDialog()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = {
            if (showPermissionDeniedSnackbar) {
                Snackbar(
                    action = {
                        TextButton(onClick = { showPermissionDeniedSnackbar = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text("Permiso denegado. Ve a Configuración para habilitarlo.")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- FOTO DE PERFIL ---
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onShowImageSourceDialog() },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.profile.fotoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.profile.fotoUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar por defecto",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { onShowImageSourceDialog() }) {
                Text("Cambiar foto de perfil")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- NOMBRE DE USUARIO ---
            OutlinedTextField(
                value = uiState.profile.nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- PREFERENCIAS ---
            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Modo oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Modo oscuro",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Activar tema oscuro en la aplicación",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.profile.modoOscuro,
                    onCheckedChange = onModoOscuroToggle
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // Notificaciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Recibir notificaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Notificaciones sobre nuevos temas y respuestas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = uiState.profile.recibirNotificaciones,
                    onCheckedChange = onNotificacionesToggle
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💡 Tip",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tus preferencias se guardan automáticamente y persisten incluso si cierras la aplicación.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // --- DIÁLOGO DE SELECCIÓN DE FUENTE DE IMAGEN ---
        if (uiState.showImageSourceDialog) {
            AlertDialog(
                onDismissRequest = onHideImageSourceDialog,
                title = { Text("Seleccionar foto") },
                text = { Text("¿De dónde quieres obtener la foto?") },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Botón Cámara
                        OutlinedButton(
                            onClick = {
                                val permission = Manifest.permission.CAMERA
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        permission
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        cameraLauncher.launch(photoUri)
                                    }
                                    else -> {
                                        cameraPermissionLauncher.launch(permission)
                                    }
                                }
                            }
                        ) {
                            Text("📷 Cámara")
                        }

                        // Botón Galería
                        OutlinedButton(
                            onClick = {
                                // Android 13+ requiere READ_MEDIA_IMAGES
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val permission = Manifest.permission.READ_MEDIA_IMAGES
                                    when {
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            permission
                                        ) == PackageManager.PERMISSION_GRANTED -> {
                                            galleryLauncher.launch("image/*")
                                        }
                                        else -> {
                                            galleryPermissionLauncher.launch(permission)
                                        }
                                    }
                                } else {
                                    // Versiones anteriores no requieren permiso para galería
                                    galleryLauncher.launch("image/*")
                                }
                            }
                        ) {
                            Text("🖼️ Galería")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = onHideImageSourceDialog) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Perfil - Usuario sin foto")
@Composable
fun PerfilScreenPreview() {
    ProyectoForoCineTheme {
        PerfilScreen(
            uiState = PerfilUiState(
                profile = UserProfile(
                    nombre = "CinéfiloExperto",
                    fotoUri = null,
                    modoOscuro = false,
                    recibirNotificaciones = true
                )
            ),
            onNombreChange = {},
            onFotoSeleccionada = {},
            onModoOscuroToggle = {},
            onNotificacionesToggle = {},
            onShowImageSourceDialog = {},
            onHideImageSourceDialog = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Perfil - Modo oscuro activado")
@Composable
fun PerfilScreenDarkModePreview() {
    ProyectoForoCineTheme(darkTheme = true) {
        PerfilScreen(
            uiState = PerfilUiState(
                profile = UserProfile(
                    nombre = "UsuarioOscuro",
                    fotoUri = null,
                    modoOscuro = true,
                    recibirNotificaciones = false
                )
            ),
            onNombreChange = {},
            onFotoSeleccionada = {},
            onModoOscuroToggle = {},
            onNotificacionesToggle = {},
            onShowImageSourceDialog = {},
            onHideImageSourceDialog = {},
            onNavigateBack = {}
        )
    }
}
