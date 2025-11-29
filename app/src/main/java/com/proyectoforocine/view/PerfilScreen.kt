package com.proyectoforocine.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.data.local.UsuarioEntity
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.viewmodel.PerfilUiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    uiState: PerfilUiState,
    onNavigateBack: () -> Unit,
    onMisTemasClick: () -> Unit
) {
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
                // --- Sección de Usuario ---
                Icon(Icons.Default.AccountCircle, contentDescription = "Avatar", modifier = Modifier.size(100.dp))
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
            uiState = PerfilUiState(usuario = fakeUser, temasUsuario = List(5) { com.proyectoforocine.data.local.Tema(it.toLong(), "", "", 1) }),
            onNavigateBack = {},
            onMisTemasClick = {}
        )
    }
}