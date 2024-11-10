package com.air_wheelly.wheelly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.air_wheelly.wheelly.data.repository.AuthRepositoryImpl
import com.air_wheelly.wheelly.presentation.auth.RegisterScreen
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import com.air_wheelly.wheelly.util.RetrofitInstance

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitInstance.authService

        val authRepository = AuthRepositoryImpl(apiService)

        setContent {
            WheellyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    RegisterScreen(repo = authRepository)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WheellyTheme {
        Greeting("Android")
    }
}