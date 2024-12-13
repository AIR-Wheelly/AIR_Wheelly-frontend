package hr.air_wheelly.ws.request_handlers

import hr.air_wheelly.core.network.ResponseCarList
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class CarListRequestHandler : TemplateRequestHandler<ResponseCarList>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<ResponseCarList>> {
        val service = NetworkService.carListService

        return service.getListedCars("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6Ijc5YjgxYzEwLWUxN2YtNDhlZi1iMjhlLWYxNGJmMmU0Y2JlNCIsIm5iZiI6MTczNDEyOTI2NSwiZXhwIjoxNzM2NzIxMjY1LCJpYXQiOjE3MzQxMjkyNjUsImlzcyI6IndoZWVsbHkiLCJhdWQiOiJ1c2VycyJ9.iib2cRGZTjzF2e90pGdvlC8Ym9e5RuaeP6LyQoBU6gs")
    }
}