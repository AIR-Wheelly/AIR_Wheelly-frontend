package com.air_wheelly.wheelly

import android.os.Bundle
import android.util.Log
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
import com.air_wheelly.wheelly.presentation.payment.PaymentScreen
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInListener
import com.braintreepayments.api.DropInResult
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.body.CreatePurchaseBody
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.models.responses.payment.CreatePurchaseResponse
import hr.air_wheelly.ws.request_handlers.CreateTransactionRequestHandler

class MainActivity : FragmentActivity(), DropInListener {
    private lateinit var createPurchase: () -> Unit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenizationKey = "sandbox_w3fdp93n_tmcvwzknv5w6689h" //TODO get clientId
        val dropInClient = DropInClient(this, tokenizationKey)
        dropInClient.setListener(this)

        createPurchase = {
            val createPurchaseBody = CreatePurchaseBody(
                paymentMethodNonce = "fake-valid-nonce",
                deviceData = null,
                amount = 20.00f,
                reservationId = "01945708-1fc1-7c02-ba90-dbf865ca7804"
            )

            val handler = CreateTransactionRequestHandler(this, createPurchaseBody)

            handler.sendRequest(
                object : ResponseListener<CreatePurchaseResponse> {
                    override fun onSuccessfulResponse(response: SuccessfulResponseBody<CreatePurchaseResponse>) {
                        Log.d("PAYMENT", "Payment was successfull")
                    }

                    override fun onErrorResponse(response: ErrorResponseBody) {
                        Log.d("PAYMENT", "Payment NOT successful")
                    }

                    override fun onNetworkFailure(t: Throwable) {
                        Log.d("ERROR", t.cause.toString())
                    }
                }
            )
        }

        setContent {
            WheellyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    var user by remember { mutableStateOf<ProfileResponse?>(null) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    /*AppNavigator(navController, user, errorMessage, dropInClient, createPurchase) { profile ->
                        user = profile
                        navController.navigate("createListing") {
                            popUpTo("login") { inclusive = true }
                        }
                    }*/

                    PaymentScreen(navController, dropInClient)
                }
            }
        }
    }

    override fun onDropInSuccess(dropInResult: DropInResult) {
        Toast.makeText(this, "Drop In Success", Toast.LENGTH_SHORT).show()
        createPurchase()
    }

    override fun onDropInFailure(error: Exception) {
        Toast.makeText(this, "Drop In Failure", Toast.LENGTH_SHORT).show()
    }
}