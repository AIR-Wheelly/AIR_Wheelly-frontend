package hr.air_wheelly.ws.network

import android.content.Context
import android.util.Log
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import hr.air_wheelly.ws.models.ChatMessage
import hr.air_wheelly.ws.models.TokenManager

class ChatService(private val context: Context, private val reservationId: String) {
    private lateinit var hubConnection: HubConnection
    private var onMessageReceived: ((ChatMessage) -> Unit)? = null

    fun startConnection() {
        val token = TokenManager.getToken(context)
        hubConnection = HubConnectionBuilder.create("http://10.0.2.2:8080/api/chathub")
            .withHeader("Authorization", "Bearer $token")
            .build()

        hubConnection.on("ReceiveMessage", { message: ChatMessage ->
            Log.d("SignalR", "Received message: $message")
            onMessageReceived?.invoke(message)
        }, ChatMessage::class.java)

        hubConnection.start().blockingAwait()
        hubConnection.send("JoinGroup", reservationId)
    }

    fun sendMessage(message: String) {
        hubConnection.send("SendMessage", reservationId, message)
    }

    fun stopConnection() {
        hubConnection.stop().blockingAwait()
    }

    fun setOnMessageReceivedListener(listener: (ChatMessage) -> Unit) {
        onMessageReceived = listener
    }
}