package ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.mvvm.splash_screen.SplashScreen
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ForegroundConnectService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    private val socketManager = SocketManager.getInstance()

    fun getIsServiceStarted() = isServiceStarted

    companion object {
        // For Singleton instantiation
        private var instance: ForegroundConnectService? = null

        fun getInstance() = instance ?: ForegroundConnectService().also { instance = it }
    }

    override fun onBind(intent: Intent): IBinder? {
        log("Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log("This should never happen. No action in the received intent")
            }
        } else {
            log(
                    "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        log("The service has been created".toUpperCase(Locale.getDefault()))
    }

    override fun onDestroy() {
        super.onDestroy()
        log("The service has been destroyed".toUpperCase(Locale.getDefault()))
    }

    private fun startService() {
        if (isServiceStarted) return
        log("Starting the foreground service task")
        isServiceStarted = true

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                        acquire()
                    }
                }

        var notification = createNotification(getString(R.string.notificationTitleConnectedService))
        startForeground(1, notification)

        // we're starting the socket.io
        socketManager.connectToServer()

        GlobalScope.launch(Dispatchers.Default) {
            while (isServiceStarted) {
                if (!isNetConnected()) {
                    notification = createNotification(getString(R.string.notificationTitleDisconnectedService))
                    socketManager.disconnectFromServer()
                } else {
                    if (!socketManager.isSocketConnected) {
                        socketManager.connectToServer()
                    }
                    notification = createNotification(getString(R.string.notificationTitleConnectedService))
                }
                val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.notify(1, notification)
            }
        }
    }

    private fun isNetConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private fun stopService() {
        log("Stopping the foreground service")
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
            socketManager.disconnectFromServer()
            socketManager.isSocketConnected = false
        } catch (e: Exception) {
            log("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
    }

    private fun createNotification(title: String): Notification {
        val notificationChannelId = getString(R.string.notificationChannel)

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                    notificationChannelId,
                    getString(R.string.notificationChannelName),
                    NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = getString(R.string.notificationChannelDesc)
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, SplashScreen::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
        ) else Notification.Builder(this)

        return builder
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
                .build()
    }

    private fun log(message: String) {
        Log.d(this::class.java.simpleName, message)
    }

}
