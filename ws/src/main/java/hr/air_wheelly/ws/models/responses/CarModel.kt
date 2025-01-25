package hr.air_wheelly.ws.models.responses

import com.google.gson.annotations.SerializedName

data class CarModel(
    val id: String,
    @SerializedName("manafacturerId") val manufacturerId: String,
    val name: String,
    @SerializedName("manafacturerName") val manufacturerName: String
)
