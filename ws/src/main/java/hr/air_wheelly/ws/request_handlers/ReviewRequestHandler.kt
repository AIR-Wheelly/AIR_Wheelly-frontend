package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.models.body.ReviewBody
import hr.air_wheelly.ws.models.responses.ReviewResponse
import hr.air_wheelly.ws.network.NetworkService
import retrofit2.Call

class ReviewRequestHandler(private val context: Context, private val reviewBody: ReviewBody) : TemplateRequestHandler<ReviewResponse>() {
    override fun getServiceCall(): Call<SuccessfulResponseBody<ReviewResponse>> {
        val service = NetworkService.reviewService(context)
        return service.createReview(reviewBody)
    }
}