package hr.air_wheelly.ws.models
import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "prefs"
    private const val TOKEN_KEY = "token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        return getPreferences(context).getString(TOKEN_KEY, null)
    }
}