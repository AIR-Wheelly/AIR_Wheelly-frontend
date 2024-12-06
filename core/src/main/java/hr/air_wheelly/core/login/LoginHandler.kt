package hr.air_wheelly.core.login

interface LoginHandler {
    fun handleLogin(loginToken: LoginToken, loginListener: LoginOutcomeListener)
}