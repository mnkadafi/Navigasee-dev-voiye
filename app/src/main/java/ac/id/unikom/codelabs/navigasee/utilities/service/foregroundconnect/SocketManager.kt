package ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect

import ac.id.unikom.codelabs.navigasee.BuildConfig
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import io.socket.client.IO
import io.socket.client.Socket

class SocketManager {

    private var socket: Socket? = null
    var isSocketConnected = false

    private val preferences = Preferences.getInstance()
    private var locationManager: LocationManager? = null

    fun connectToServer() {
        connectToSocket()
        locationManager = LocationManager.getInstance()
        locationManager!!.getMyLocation()
    }

    fun disconnectFromServer() {
        if (locationManager != null) {
            locationManager!!.stopMyLocation()
            socket?.disconnect()
            locationManager = null
            isSocketConnected = false
        }
    }

    private fun connectToSocket() {
        try {
            socket = IO.socket(BuildConfig.BASE_URL)
            socket?.connect()

            val email = preferences.getEmail()
            socket?.emit("user", email)

            isSocketConnected = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        // For Singleton instantiation
        private var instance: SocketManager? = null

        fun getInstance() = instance
                ?: SocketManager().also { instance = it }
    }
}