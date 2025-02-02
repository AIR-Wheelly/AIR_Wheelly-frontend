package com.air_wheelly.wheelly

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.air_wheelly.wheelly.ui.theme.WheellyTheme
import com.air_wheelly.wheelly.util.AppNavigator
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInListener
import com.braintreepayments.api.DropInResult
import hr.air_wheelly.ws.models.TokenManager
import hr.air_wheelly.ws.models.body.CreatePurchaseBody
import hr.air_wheelly.ws.models.responses.ProfileResponse
import hr.air_wheelly.ws.models.responses.payment.CreatePurchaseResponse
import hr.air_wheelly.ws.network.NotificationService
import hr.air_wheelly.ws.request_handlers.CreateTransactionRequestHandler
import hr.air_wheelly.ws.request_handlers.ProfileRequestHandler
import hr.air_wheelly.core.network.ResponseListener
import hr.air_wheelly.core.network.models.ErrorResponseBody
import hr.air_wheelly.core.network.models.SuccessfulResponseBody

class MainActivity : FragmentActivity(), DropInListener {

    private lateinit var onPurchaseInit: (String, Float) -> Unit
    private lateinit var createPurchase: () -> Unit
    private lateinit var createPurchaseBody: CreatePurchaseBody

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenizationKey = "sandbox_w3fdp93n_tmcvwzknv5w6689h"
        val dropInClient = DropInClient(this, tokenizationKey)
        dropInClient.setListener(this)
        startNotificationService(this)

        onPurchaseInit = { reservationId, amount ->
            createPurchaseBody = CreatePurchaseBody(
                paymentMethodNonce = "fake-valid-nonce",
                deviceData = null,
                amount = amount,
                reservationId = reservationId
            )
        }

        createPurchase = {
            val handler = CreateTransactionRequestHandler(this, createPurchaseBody)
            handler.sendRequest(object : ResponseListener<CreatePurchaseResponse> {
                override fun onSuccessfulResponse(response: SuccessfulResponseBody<CreatePurchaseResponse>) {
                    Log.d("PAYMENT", "Payment was successful")
                    Toast.makeText(this@MainActivity, "Payment Successful", Toast.LENGTH_SHORT).show()
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    Log.d("PAYMENT", "Payment NOT successful")
                    Toast.makeText(this@MainActivity, "Error, not paid", Toast.LENGTH_SHORT).show()
                }

                override fun onNetworkFailure(t: Throwable) {
                    Log.d("ERROR", t.cause.toString())
                    Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        setContent {
            WheellyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()

                    var user by remember { mutableStateOf<ProfileResponse?>(null) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }
                    var isLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        val token = TokenManager.getToken(context)
                        if (!token.isNullOrEmpty()) {
                            val profileRequestHandler = ProfileRequestHandler(context)
                            profileRequestHandler.sendRequest(object : ResponseListener<ProfileResponse> {
                                override fun onSuccessfulResponse(
                                    response: SuccessfulResponseBody<ProfileResponse>
                                ) {
                                    user = response.result
                                    isLoading = false
                                }

                                override fun onErrorResponse(response: ErrorResponseBody) {
                                    errorMessage = "Error loading profile."
                                    isLoading = false
                                }

                                override fun onNetworkFailure(t: Throwable) {
                                    errorMessage = "Network error."
                                    isLoading = false
                                }
                            })
                        } else {
                            isLoading = false
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        AppNavigator(
                            navController = navController,
                            user = user,
                            errorMessage = errorMessage,
                            dropInClient = dropInClient,
                            onPurchaseInit = onPurchaseInit
                        ) { profile ->
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

    private fun startNotificationService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf("android.permission.POST_NOTIFICATIONS"),
                    1001
                )
            }
        }
        val serviceIntent = Intent(context, NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
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