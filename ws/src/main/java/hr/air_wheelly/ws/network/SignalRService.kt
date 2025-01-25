package hr.air_wheelly.ws.network

import android.content.Context
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import hr.air_wheelly.ws.models.ChatMessage
import hr.air_wheelly.ws.models.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignalRService(private val context: Context, private val reservationId: String) {
    private lateinit var hubConnection: HubConnection

    fun startConnection() {
        val token = TokenManager.getToken(context)
        hubConnection = HubConnectionBuilder.create("http://10.0.2.2:8080/api/chathub")
            .withHeader("Authorization", "Bearer $token")
            .build()

        hubConnection.on("ReceiveMessage", { message: ChatMessage ->

        }, ChatMessage::class.java)

        hubConnection.start().blockingAwait()
        hubConnection.send("JoinGroup", reservationId)
    }

    suspend fun sendMessage(message: String) {
        withContext(Dispatchers.IO) {
            hubConnection.send("SendMessage", reservationId, message)
        }
    }

    fun stopConnection() {
        hubConnection.stop().blockingAwait()
    }
}