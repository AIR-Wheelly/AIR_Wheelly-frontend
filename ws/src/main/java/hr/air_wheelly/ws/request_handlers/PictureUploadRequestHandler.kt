package hr.air_wheelly.ws.request_handlers

import android.content.Context
import hr.air_wheelly.core.network.models.SuccessfulResponseBody
import hr.air_wheelly.ws.network.NetworkService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class UploadCarImagesRequestHandler(
    private val context: Context,
    private val listingId: RequestBody,
    private val files: List<MultipartBody.Part>
) : TemplateRequestHandler<Unit>() {

    override fun getServiceCall(): Call<SuccessfulResponseBody<Unit>> {
        val service = NetworkService.carService(context)
        return service.uploadCarImages(listingId, files)
    }
}