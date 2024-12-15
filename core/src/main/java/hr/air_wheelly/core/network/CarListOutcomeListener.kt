package hr.air_wheelly.core.network

interface CarListOutcomeListener {
    fun onSuccessfulCarListFetch(response: List<CarListResponse>)
    fun onFailedCarListFetch(reason: String)
}