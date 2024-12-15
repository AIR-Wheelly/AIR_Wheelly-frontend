package com.air_wheelly.wheelly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import com.air_wheelly.wheelly.util.AppNavigator
import hr.air_wheelly.ws.models.responses.ProfileResponse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WheellyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    var user by remember { mutableStateOf<ProfileResponse?>(null) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    AppNavigator(navController, user, errorMessage) { profile ->
                        user = profile
                        navController.navigate("createListing") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}