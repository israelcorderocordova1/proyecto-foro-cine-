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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.proyectoforocine.R
import com.proyectoforocine.data.local.UsuarioEntity
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.viewmodel.PerfilUiStateNew
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    uiState: PerfilUiStateNew,
    onNavigateBack: () -> Unit,
    onMisTemasClick: () -> Unit,
    onFotoSeleccionada: (Uri?) -> Unit = {},
    onModoOscuroToggle: (Boolean) -> Unit = {},
    onNotificacionesToggle: (Boolean) -> Unit = {},
    onShowImageSourceDialog: () -> Unit = {},
    onHideImageSourceDialog: () -> Unit = {}
) {
    val context = LocalContext.current
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
                }
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
            if (uiState.usuario != null) {
                // --- FOTO DE PERFIL CON CÁMARA/GALERÍA ---
                Box(
                    modifier = Modifier
                        .size(120.dp)
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
                            contentDescription = "Avatar",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(onClick = { onShowImageSourceDialog() }) {
                    Text("Cambiar foto de perfil")
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(uiState.usuario.username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(uiState.usuario.email, style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                
                // --- Sección de Estadísticas ---
                Spacer(modifier = Modifier.height(16.dp))
                Text("Estadísticas", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(16.dp))

                StatItem(icon = Icons.Default.DateRange, label = "Miembro desde", value = formatDate(uiState.usuario.registrationDate))
                StatItem(icon = Icons.AutoMirrored.Filled.ListAlt, label = "Temas creados", value = uiState.temasUsuario.size.toString())

                Spacer(modifier = Modifier.height(24.dp))
                Divider()

                // --- Preferencias ---
                Spacer(modifier = Modifier.height(16.dp))
                Text("Preferencias", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Start))
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
                        onCheckedChange = onModoOscuroToggle,
                        modifier = Modifier.testTag("darkModeToggle")
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
                Divider()

                // --- Sección de Actividad ---
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onMisTemasClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                    Text("Ver mis temas creados")
                }
            } else {
                // Muestra un mensaje si no se pudo cargar el usuario
                Text("No se pudo cargar la información del perfil.")
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
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.add_a_photo_24px),
                                    contentDescription = "Cámara"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Cámara")
                            }
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
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.add_photo_alternate_24px),
                                    contentDescription = "Galería"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Galería")
                            }
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

@Composable
private fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.bodyLarge)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    ProyectoForoCineTheme {
        val fakeUser = UsuarioEntity(
            id = 1,
            username = "Israel",
            email = "israel@test.com",
            password = "",
            rol = "registrado",
            registrationDate = System.currentTimeMillis()
        )
        PerfilScreen(
            uiState = PerfilUiStateNew(usuario = fakeUser, temasUsuario = List(5) { com.proyectoforocine.data.local.Tema(it.toLong(), "", "", 1) }),
            onNavigateBack = {},
            onMisTemasClick = {}
        )
    }
}