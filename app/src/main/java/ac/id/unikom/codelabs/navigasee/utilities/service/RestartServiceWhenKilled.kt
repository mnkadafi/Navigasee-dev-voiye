package ac.id.unikom.codelabs.navigasee.utilities.service

import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect.ForegroundConnectService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class RestartServiceWhenKilled : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val preferences = Preferences.getInstance()
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && !ForegroundConnectService.getInstance().getIsServiceStarted()) {
            Intent(context, ForegroundConnectService::class.java).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(this::class.java.simpleName, "startService: Starting the service in >=26 Mode")
                    context.startForegroundService(it)
                    return
                }
                Log.d(this::class.java.simpleName, "startService: Starting the service in < 26 Mode")
                context.startService(it)
            }
        }

    }
}
