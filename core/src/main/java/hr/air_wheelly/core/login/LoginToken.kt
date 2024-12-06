package hr.air_wheelly.core.login

interface LoginToken {
    fun getAuthorizers(): Map<String, String>
}