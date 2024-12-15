package hr.air_wheelly.ws.models

import com.google.gson.annotations.SerializedName

data class LoggedInUserJWT(
    @SerializedName("token") var token: String? = null
)
