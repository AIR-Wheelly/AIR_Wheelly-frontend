package hr.air_wheelly.login_email_password

import hr.air_wheelly.core.login.ILoginConfig

data class EmailPasswordLoginConfig(
    val email: String,
    val password: String
): ILoginConfig
