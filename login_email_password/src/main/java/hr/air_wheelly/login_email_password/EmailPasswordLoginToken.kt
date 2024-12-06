package hr.air_wheelly.login_email_password

import hr.air_wheelly.core.login.LoginToken

class EmailPasswordLoginToken(email: String, password: String) : LoginToken {

    private val authorizers = mapOf(
        "email" to email,
        "password" to password,
    )

    override fun getAuthorizers(): Map<String, String> {
        return authorizers
    }
}