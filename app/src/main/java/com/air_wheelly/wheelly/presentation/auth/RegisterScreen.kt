package com.air_wheelly.wheelly.presentation.auth

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.model.User
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.RegistrationBody
import hr.air_wheelly.ws.models.responses.RegisterResponse
import hr.air_wheelly.ws.request_handlers.RegistrationRequestHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val _errorMessage: MutableLiveData<String> = MutableLiveData("")

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun onRegisterClicked() {
        emailError = ""
        passwordError = ""
        confirmPasswordError = ""

        if (firstName.isEmpty()) {
            firstNameError = "First name cannot be empty"
        }
        if (lastName.isEmpty()) {
            lastNameError = "Last name cannot be empty"
        }
        if (email.isEmpty()) {
            emailError = "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Invalid email format"
        }
        if (password.isEmpty()) {
            passwordError = "Password cannot be empty"
        }
        if (confirmPassword != password) {
            confirmPasswordError = "Passwords do not match"
        }

        if (emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
            scope.launch {
                loading = true

                val requestBody = RegistrationBody(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )

                try {
                    val registrationRequestHandler = RegistrationRequestHandler(requestBody, context)

                    registrationRequestHandler.sendRequest(object: ResponseListener<RegisterResponse> {
                        override fun onSuccessfulResponse(response: SuccessfulResponseBody<RegisterResponse>) {
                            Toast.makeText(context, "You have been registerd", Toast.LENGTH_SHORT).show()
                        }

                        override fun onErrorResponse(response: ErrorResponseBody) {
                            _errorMessage.value = response.message + " "
                            _errorMessage.value += when (response.error_code) {
                                101 -> "Check username."
                                102 -> "Username is already used. Please enter another one."
                                103 -> "Email is invalid."
                                104 -> "Email entered is already used. Do you already have an account?"
                                105 -> "Password is invalid. Make sure it has at least 7 characters with at least 1 number."
                                106 -> "Selected role is invalid!"
                                107 -> "First name is invalid!"
                                108 -> "Last name is invalid!"
                                else -> ""
                            }

                            Toast.makeText(context, _errorMessage.value, Toast.LENGTH_SHORT).show()
                        }

                        override fun onNetworkFailure(t: Throwable) {
                            _errorMessage.value = "Network error occured, please try again later..."
                            Toast.makeText(context, _errorMessage.value, Toast.LENGTH_SHORT).show()
                        }
                    })
                    navController.navigate("login")
                } catch (e: HttpException) {
                    Toast.makeText(context, "Unable to register!", Toast.LENGTH_LONG).show()
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
            Text("Registration", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                isError = firstNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (firstNameError.isNotEmpty()) {
                Text(
                    text = firstNameError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                isError = lastNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (lastNameError.isNotEmpty()) {
                Text(
                    text = lastNameError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onRegisterClicked() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text("Register")
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
                navController.navigate("login")
            }) {
                Text("Already have an account? Login")
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    WheellyTheme {
        RegisterScreen()
    }
}*/
