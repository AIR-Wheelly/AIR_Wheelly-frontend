package hr.air_wheelly.ws.models

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "prefs"
    private const val TOKEN_KEY = "token"
    const val ACTION_USER_LOGGED_IN = "hr.air_wheelly.USER_LOGGED_IN"
    const val ACTION_USER_LOGGED_OUT = "hr.air_wheelly.USER_LOGGED_OUT"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun isUserSignedIn(context: Context): Boolean {
        return !getToken(context).isNullOrEmpty()
    }

    fun getToken(context: Context): String? {
        return getPreferences(context).getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }
}