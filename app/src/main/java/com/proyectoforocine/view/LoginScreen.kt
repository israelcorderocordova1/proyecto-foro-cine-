package com.proyectoforocine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme
import com.proyectoforocine.viewmodel.LoginUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio de Sesión") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
            Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
                Text("Iniciar Sesión")
            }
            if (uiState.loginError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(uiState.loginError, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onNavigateToForgotPassword) {
                Text("¿Olvidaste tu contraseña?")
            }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    ProyectoForoCineTheme {
        LoginScreen(
            uiState = LoginUiState(emailError = "Email inválido"),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onNavigateToRegister = {},
            onNavigateToForgotPassword = {}
        )
    }
}