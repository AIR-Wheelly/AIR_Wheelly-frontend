package hr.air_wheelly.core.network

interface RequestHandler<T> {
    fun sendRequest(responseListener: ResponseListener<T>)
}