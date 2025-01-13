package com.air_wheelly.wheelly.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.air_wheelly.wheelly.domain.model.ProfileEditHandler
import hr.air_wheelly.ws.models.UpdateProfileRequest
import androidx.compose.ui.platform.LocalContext
import com.air_wheelly.wheelly.presentation.components.BottomNavigation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val profileEditHandler = remember { ProfileEditHandler(context) }

    // Observe user profile and error messages
    val userProfile by profileEditHandler.userProfile.collectAsState()
    val errorMessage by profileEditHandler.errorMessage.collectAsState()

    // Local state for profile editing
    var firstName by remember { mutableStateOf(userProfile?.firstName ?: "") }
    var lastName by remember { mutableStateOf(userProfile?.lastName ?: "") }
    var email by remember { mutableStateOf(userProfile?.email ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    // Layout
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Loading state
        if (userProfile == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Display the fetched user profile
            userProfile?.let {
                Text("First Name: ${it.firstName}")
                Text("Last Name: ${it.lastName}")
                Text("Email: ${it.email}")

                Spacer(modifier = Modifier.height(16.dp))

                // Edit fields with current user profile data
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") }
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") }
                )
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                TextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") }
                )
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button to update profile
                Button(onClick = {
                    val updateProfileRequest = UpdateProfileRequest(
                        firstName, lastName, email, currentPassword, newPassword
                    )
                    profileEditHandler.updateProfile(updateProfileRequest)
                }) {
                    Text("Save Changes")
                }
            }
        }

        // Display error message if there is any
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Spacer to push the BottomNavigation to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Add Bottom Navigation
        BottomNavigation(
            navController = navController,
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp)
                .fillMaxWidth()
        )
    }
}

