package hr.air_wheelly.core.network

interface CarHandler {
    suspend fun getCarListings(carListOutcomeListener: CarListOutcomeListener)
}