package com.air_wheelly.wheelly.domain.model

import android.content.Context
import hr.air_wheelly.ws.models.responses.car.CreateNewReservationBody
import hr.air_wheelly.ws.request_handlers.CarReservationRequestHandler
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.core.network.models.ErrorResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CarReservationModel(
    private val startDate: LocalDate,
    private val endDate: LocalDate,
    private val rentalPricePerDay: Double
) {
    fun calculateTotalPrice(): Double {
        val days = ChronoUnit.DAYS.between(startDate, endDate)
        return days * rentalPricePerDay
    }

    suspend fun createReservation(context: Context, carListingId: String): Result<Unit> {
        val reservationBody = CreateNewReservationBody(
            carListingId = carListingId,
            startDate = startDate.toString(),
            endDate = endDate.toString()
        )
        val handler = CarReservationRequestHandler(context, reservationBody)
        return withContext(Dispatchers.IO) {
            try {
                val response = handler.getServiceCall().execute()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to create reservation"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}