@file:OptIn(ExperimentalMaterial3Api::class)

package com.air_wheelly.wheelly.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.login.LoginViewModel
import com.air_wheelly.wheelly.domain.login.LoginViewModelFactory
import hr.air_wheelly.login_google.GoogleLoginConfig
import hr.air_wheelly.ws.models.responses.ProfileResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (ProfileResponse) -> Unit
) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context, onLoginSuccess))
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = state.emailError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if(state.emailError != null) {
                Text(
                    text = state.emailError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = state.passwordError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.passwordError != null) {
                Text(
                    text = state.passwordError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.validateInputs(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Login")
            }

            Button(
                onClick = {
                    val googleLoginConfig = GoogleLoginConfig(context)
                    viewModel.performLogin(googleLoginConfig)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Google Login")
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            TextButton(onClick = {
                navController.navigate("registration")
            }) {
                Text("Don't have an account? Register")
            }
        }
    }
}