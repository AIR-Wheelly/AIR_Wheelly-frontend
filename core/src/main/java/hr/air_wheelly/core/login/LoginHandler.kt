package hr.air_wheelly.core.login

interface LoginHandler {
    suspend fun handleLogin(loginConfig: ILoginConfig, loginOutcomeListener: LoginOutcomeListener)
}