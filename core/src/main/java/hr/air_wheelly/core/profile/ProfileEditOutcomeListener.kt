package hr.air_wheelly.core.profile

interface ProfileEditOutcomeListener {
    fun onSuccessfulProfileEdit(message: String)
    fun onFailedProfileEdit(message: String)
}
