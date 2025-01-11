package com.air_wheelly.wheelly.presentation.payment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.braintreepayments.api.DropInClient
import com.braintreepayments.api.DropInRequest

@Composable
fun PaymentScreen(
    navController: NavController,
    dropInClient: DropInClient?
) {
    val context = LocalContext.current
    val dropInRequest = DropInRequest()

    val mockedRentData = MockedRentData(
        daysRented = 2,
        pricePerDay = 10.00f,
        renterId = "renterID",
        ownerId = "ownerID"
    )

    fun payForCarRent() {
        Toast.makeText(context, "Start Process", Toast.LENGTH_SHORT).show()
        if (dropInClient != null) {
            dropInClient.launchDropIn(dropInRequest)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Price Per Day: ${mockedRentData.pricePerDay}€") //TODO get price per day
                            Text("Days Rented: ${mockedRentData.daysRented}") //TODO get days rented
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            val amount = mockedRentData.daysRented * mockedRentData.pricePerDay
                            Text("Amount to pay: ${amount}") //TODO get real amount
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { payForCarRent() }
                ) {
                    Text("Pay")
                }
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

data class MockedRentData(
    val daysRented: Int,
    val pricePerDay: Float,
    val renterId: String,
    val ownerId: String
)