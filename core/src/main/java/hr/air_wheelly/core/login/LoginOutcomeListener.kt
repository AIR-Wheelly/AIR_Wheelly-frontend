package hr.air_wheelly.core.login

interface LoginOutcomeListener {
    fun onSuccessfulLogin(loginResponse: LoginResponse)
    fun onFailedLogin(reason: String)
}