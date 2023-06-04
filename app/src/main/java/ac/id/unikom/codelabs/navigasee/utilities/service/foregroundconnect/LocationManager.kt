package ac.id.unikom.codelabs.navigasee.utilities.service.foregroundconnect

import ac.id.unikom.codelabs.navigasee.MyApplication
import ac.id.unikom.codelabs.navigasee.data.source.UpdateStatusRepository
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationManager {

    private val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MyApplication.getContext())
    private var locationCallback: LocationCallback
    private val preferences = Preferences.getInstance()
    private var locationRequest: LocationRequest

    init {

        locationRequest = LocationRequest().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            val INTERVAL = 3000L
            val FASTEST_INTERVAL = INTERVAL / 2
            interval = INTERVAL

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval = FASTEST_INTERVAL

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
//            maxWaitTime = TimeUnit.MINUTES.toMillis(1)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            smallestDisplacement = 10F
        }

        // Step 1.4, Initialize the LocationCallback.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {

                    // Normally, you want to save a new location to a database. We are simplifying
                    // things a bit and just saving it as a local variable, as we only need it again
                    // if a Notification is created (when user navigates away from app).
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(36 * 100)

                        preferences.saveLocation(locationResult.lastLocation.longitude.toString(), locationResult.lastLocation.latitude.toString(), "")

                        val repo = UpdateStatusRepository.getInstance()
                        val lat = preferences.getLatitude()?.toDouble()
                        val long = preferences.getLongitude()?.toDouble()

                        repo.updateStatus(1, lat!!, long!!)
                    }

                } else {
                    Log.d(this::class.java.simpleName, "Location information isn't available.")
                }
            }
        }
    }

    fun stopMyLocation() {
        try {
            // Step 1.6, Unsubscribe to location changes.
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(this::class.java.simpleName, "Location Callback removed.")
                } else {
                    Log.d(this::class.java.simpleName, "Failed to remove Location Callback.")
                }
            }

        } catch (unlikely: SecurityException) {
            Log.d(this::class.java.simpleName, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    fun getMyLocation() {

        try {
            // Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

        } catch (unlikely: SecurityException) {
            Log.e(this::class.java.simpleName, "Lost location permissions. Couldn't remove updates. $unlikely")
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    companion object {
        // For Singleton instantiation
        private var instance: LocationManager? = null

        fun getInstance() = instance
                ?: LocationManager().also { instance = it }
    }
}