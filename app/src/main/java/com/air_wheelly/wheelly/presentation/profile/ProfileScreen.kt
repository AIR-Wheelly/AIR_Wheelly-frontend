package com.air_wheelly.wheelly.presentation.profile

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    navController: NavController
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    fun validateInput(): Boolean {
        firstNameError = if (firstName.isBlank()) "First name cannot be empty" else ""
        lastNameError = if (lastName.isBlank()) "Last name cannot be empty" else ""
        emailError = if (email.isBlank()) {
            "Email cannot be empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Invalid email format"
        } else ""

        return firstNameError.isEmpty() && lastNameError.isEmpty() && emailError.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title at the top
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Text fields in the middle
                Column(
                    modifier = Modifier.weight(1f, false),
                    verticalArrangement = Arrangement.Center
                ) {
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
                }

                // Divider and button at the bottom
                Column {
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (validateInput()) {
                                showDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Changes")
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Changes") },
                    text = { Text("Are you sure you want to save the changes?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            navController.navigateUp()
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }

        com.air_wheelly.wheelly.presentation.components.BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}
