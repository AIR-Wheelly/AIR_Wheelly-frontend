package com.air_wheelly.wheelly

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import com.air_wheelly.wheelly.util.AppNavigator
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInListener
import com.braintreepayments.api.DropInResult
import hr.air_wheelly.ws.models.responses.ProfileResponse

class MainActivity : FragmentActivity(), DropInListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenizationKey = "sandbox_w3fdp93n_tmcvwzknv5w6689h" //TODO get clientId
        val dropInClient = DropInClient(this, tokenizationKey)
        dropInClient.setListener(this)

        setContent {
            WheellyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    var user by remember { mutableStateOf<ProfileResponse?>(null) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    AppNavigator(navController, user, errorMessage, dropInClient) { profile ->
                        user = profile
                        navController.navigate("createListing") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            }
        }
    }

    override fun onDropInSuccess(dropInResult: DropInResult) {
        Toast.makeText(this, "Drop In Success", Toast.LENGTH_SHORT).show()
    }

    override fun onDropInFailure(error: Exception) {
        Toast.makeText(this, "Drop In Failure", Toast.LENGTH_SHORT).show()
    }
}