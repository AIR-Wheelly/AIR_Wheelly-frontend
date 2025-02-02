package com.air_wheelly.wheelly.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.profile.ProfileEditViewModel
import com.air_wheelly.wheelly.domain.profile.ProfileEditViewModelFactory
import com.air_wheelly.wheelly.presentation.components.BottomNavigation
import hr.air_wheelly.ws.models.TokenManager
import hr.air_wheelly.ws.models.UpdateProfileRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: ProfileEditViewModel = viewModel(factory = ProfileEditViewModelFactory(context))
    val state by viewModel.state.collectAsState()

    var firstName by remember { mutableStateOf(state.firstName) }
    var lastName by remember { mutableStateOf(state.lastName) }
    var currentPassword by remember { mutableStateOf(state.currentPassword) }
    var newPassword by remember { mutableStateOf(state.newPassword) }
    var showDialog by remember { mutableStateOf(false) }
    var showLogOutDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                color = Color.Black,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            state.userProfile?.let {
                Text(
                    text = "First Name: ${it.firstName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Last Name: ${it.lastName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Email: ${it.email}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes")
                }
                Button(
                    onClick = {
                        showLogOutDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Out")
                }
            }

        }
        BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text("Are you sure?")
                },
                text = {
                    Text("Do you want to save these changes?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val updateProfileRequest = UpdateProfileRequest(
                                firstName, lastName, "", currentPassword, newPassword
                            )
                            viewModel.updateProfile(updateProfileRequest)
                            showDialog = false
                            Toast.makeText(context, "Update successful! ",Toast.LENGTH_SHORT).show()
                            navController.navigate("carList")
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("No")
                    }
                }
            )
        }
        if (showLogOutDialog) {
            AlertDialog(
                onDismissRequest = { showLogOutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    Button(
                        onClick = {
                            TokenManager.clearToken(context)
                            showLogOutDialog = false
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogOutDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
