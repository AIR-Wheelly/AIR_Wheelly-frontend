package hr.air_wheelly.core.network

interface CarListOutcomeListener {
    fun onSuccessfulCarListFetch(response: ResponseCarList)
    fun onFailedCarListFetch(reason: String)
}