package hr.air_wheelly.ws.network

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.air_wheelly.wheelly.ws.R
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import hr.air_wheelly.ws.models.NotificationMessage
import hr.air_wheelly.ws.models.TokenManager
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class NotificationService : Service() {

    private lateinit var hubConnection: HubConnection
    private lateinit var broadcastReceiver: BroadcastReceiver

    companion object {
        const val CHANNEL_ID = "NotificationServiceChannel"
        const val NOTIFICATION_ID = 1001
        const val HUB_URL = "http://10.0.2.2:8080/api/notificationhub"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForegroundService()
        setupBroadcastReceiver()
        initializeSignalR()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Notification Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Background service for real-time notifications"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun startForegroundService() {
        val notification = createForegroundNotification("Initializing...")
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun setupBroadcastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    TokenManager.ACTION_USER_LOGGED_IN -> handleUserLoggedIn()
                    TokenManager.ACTION_USER_LOGGED_OUT -> handleUserLoggedOut()
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(TokenManager.ACTION_USER_LOGGED_IN)
            addAction(TokenManager.ACTION_USER_LOGGED_OUT)
        }
        registerReceiver(broadcastReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    @SuppressLint("CheckResult")
    private fun initializeSignalR() {
        hubConnection = HubConnectionBuilder.create(HUB_URL)
            .withAccessTokenProvider(Single.defer {
                val token = TokenManager.getToken(this@NotificationService)
                if (token != null) Single.just(token)
                else Single.error(Throwable("No authentication token available"))
            })
            .build()

        hubConnection.onClosed { error ->
            Log.w("NotificationService", "Connection closed: ${error?.message}")
            scheduleReconnection()
        }

        hubConnection.on("Notification", { message: NotificationMessage ->
            handleIncomingNotification(message)
        }, NotificationMessage::class.java)

        if (TokenManager.isUserSignedIn(this)) {
            connectSignalR()
        }
    }

    private fun handleUserLoggedIn() {
        Log.i("NotificationService", "User logged in - connecting SignalR")
        connectSignalR()
    }

    private fun handleUserLoggedOut() {
        Log.i("NotificationService", "User logged out - disconnecting SignalR")
        disconnectSignalR()
    }

    @SuppressLint("CheckResult")
    private fun connectSignalR() {
        if (hubConnection.connectionState == HubConnectionState.CONNECTED) return

        hubConnection.start()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.i("NotificationService", "SignalR connected successfully")
                updateForegroundNotification("Connected to notifications")
            }, { error ->
                Log.e("NotificationService", "Connection failed: ${error.message}")
                scheduleReconnection()
            })
    }

    @SuppressLint("CheckResult")
    private fun disconnectSignalR() {
        if (hubConnection.connectionState == HubConnectionState.DISCONNECTED) return

        hubConnection.stop()
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.i("NotificationService", "SignalR disconnected successfully")
                updateForegroundNotification("Disconnected from notifications")
            }, { error ->
                Log.e("NotificationService", "Disconnection failed: ${error.message}")
            })
    }

    private fun scheduleReconnection() {
        if (TokenManager.isUserSignedIn(this)) {
            Log.i("NotificationService", "Scheduling reconnection in 5 seconds...")
            android.os.Handler(mainLooper).postDelayed({
                connectSignalR()
            }, 5000)
        }
    }

    private fun handleIncomingNotification(message: NotificationMessage) {
        val title = "Wheelly Notification"
        val body = message.body

        println(message.body)

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (notificationManager == null) {
            Log.e("NotificationService", "NotificationManager is null")
            return
        }

        val broadcastIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = "hr.air_wheelly.NOTIFICATION_CLICKED"
            putExtra("notification_data", body)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createForegroundNotification(content: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Wheelly Notifications")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .build()
    }

    private fun updateForegroundNotification(content: String) {
        val notification = createForegroundNotification(content)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("CheckResult")
    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(broadcastReceiver)
            if (::hubConnection.isInitialized) {
                hubConnection.stop()
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Log.i("NotificationService", "Service destroyed - SignalR disconnected")
                    }, { error ->
                        Log.e("NotificationService", "Error stopping connection: ${error.message}")
                    })
            }
        } catch (e: Exception) {
            Log.e("NotificationService", "Cleanup error: ${e.message}")
        }

        val restartIntent = Intent(applicationContext, NotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartIntent)
        } else {
            startService(restartIntent)
        }
    }
}