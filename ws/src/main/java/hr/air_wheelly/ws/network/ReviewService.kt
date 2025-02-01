package hr.air_wheelly.ws.network

import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.body.ReviewBody
import hr.air_wheelly.ws.models.responses.ReviewResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReviewService {
    @POST("reviews")
    fun createReview(@Body reviewBody: ReviewBody) : Call<SuccessfulResponseBody<ReviewResponse>>
}