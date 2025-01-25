package hr.air_wheelly.ws.network

import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignalRService(private val reservationId: String) {
    private lateinit var hubConnection: HubConnection

    fun startConnection() {
        hubConnection = HubConnectionBuilder.create("https://your-backend-url/chathub")
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

data class ChatMessage(
    val reservationId: String,
    val senderId: String,
    val message: String
)