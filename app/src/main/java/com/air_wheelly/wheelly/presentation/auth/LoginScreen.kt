@file:OptIn(ExperimentalMaterial3Api::class)

package com.air_wheelly.wheelly.presentation.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.repository.IAuthRepository
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    repo: IAuthRepository,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun onLoginClicked() {
        emailError = ""
        passwordError = ""

        if (email.isEmpty()) {
            emailError = "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
        }

        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
        }

        if (emailError.isEmpty() && passwordError.isEmpty()) {
            scope.launch {
                loading = true
                try {
                    val user = repo.loginUser(email, password)
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show()
                    navController.navigate("home") // Navigate to the home screen or appropriate screen
                } catch (e: HttpException) {
                    Toast.makeText(context, "Unable to login!", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Network error!", Toast.LENGTH_LONG).show()
                } finally {
                    loading = false
                }
            }
        }
    }

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

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onLoginClicked() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text("Login")
            }
        }

        if (loading) {
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

/*@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    WheellyTheme {
        LoginScreen()
    }
}*/