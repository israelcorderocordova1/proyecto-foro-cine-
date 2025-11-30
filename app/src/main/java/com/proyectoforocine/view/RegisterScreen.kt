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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.viewmodel.RegisterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    uiState: RegisterUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.usernameError != null,
                supportingText = {
                    if (uiState.usernameError != null) {
                        Text(uiState.usernameError, color = Color.Red)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.emailError != null,
                supportingText = {
                    if (uiState.emailError != null) {
                        Text(uiState.emailError, color = Color.Red)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.passwordError != null,
                supportingText = {
                    if (uiState.passwordError != null) {
                        Text(uiState.passwordError, color = Color.Red)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
            if (uiState.registrationError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiState.registrationError, color = Color.Red)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    ProyectoForoCineTheme {
        RegisterScreen(
            uiState = RegisterUiState(emailError = "Email inválido", passwordError = "Mínimo 6 caracteres"),
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRegisterClick = {},
            onNavigateBack = {}
        )
    }
}
