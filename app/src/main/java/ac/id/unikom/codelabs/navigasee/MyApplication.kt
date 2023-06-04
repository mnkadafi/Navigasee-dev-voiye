package ac.id.unikom.codelabs.navigasee

import ac.id.unikom.codelabs.navigasee.utilities.ROLE_SAHABAT
import ac.id.unikom.codelabs.navigasee.utilities.ROLE_SOPIR
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect.Actions
import ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect.ForegroundConnectService
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

        startService()
    }

    fun startService() {
        val preferences = Preferences.getInstance()
        if (!ForegroundConnectService.getInstance().getIsServiceStarted() && (preferences.getRole() == ROLE_SAHABAT || preferences.getRole() == ROLE_SOPIR)) {
            Intent(this, ForegroundConnectService::class.java).also {
                it.action = Actions.START.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d(this::class.java.simpleName, "startService: Starting the service in >=26 Mode")
                    startForegroundService(it)
                    return
                }
                Log.d(this::class.java.simpleName, "startService: Starting the service in < 26 Mode")
                startService(it)
            }
        }
    }

    companion object {
        lateinit var instance: MyApplication

        fun getContext(): Context = instance.applicationContext
    }
}