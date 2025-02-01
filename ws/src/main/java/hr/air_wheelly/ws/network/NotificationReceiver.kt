package hr.air_wheelly.ws.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("NotificationReceiver", "Notification clicked, launching app...")

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        launchIntent?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra("notification_data", intent?.getStringExtra("notification_data"))
        }

        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            Log.e("NotificationReceiver", "Failed to find launch intent for package")
        }
    }
}