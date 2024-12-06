package hr.air_wheelly.ws.models

import com.google.gson.annotations.SerializedName

data class LoggedInUserJWT(
    @SerializedName("jwt") var jwt: String? = null,
)
