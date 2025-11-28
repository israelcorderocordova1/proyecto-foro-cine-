package com.proyectoforocine.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import com.proyectoforocine.ui.theme.ProyectoForoCineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginAsUser: () -> Unit,
    onLoginAsModerator: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
                Button(
                    onClick = onLoginAsUser,
                    modifier = Modifier.testTag("loginUserButton")
                ) {
                    Text("Logea como Usuario")
                }
            Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onLoginAsModerator,
                    modifier = Modifier.testTag("loginModeratorButton")
                ) {
                    Text("Logea como Moderador")
                }
        }
    }
}

@Preview(showBackground = true, name = "Login Screen Preview")
@Composable
fun LoginScreenPreview() {
    ProyectoForoCineTheme {
        LoginScreen(
            onLoginAsUser = {},
            onLoginAsModerator = {}
        )
    }
}
