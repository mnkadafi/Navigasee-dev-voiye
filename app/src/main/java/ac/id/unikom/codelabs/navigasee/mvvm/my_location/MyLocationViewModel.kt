package ac.id.unikom.codelabs.navigasee.mvvm.my_location

import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.io.IOException

class MyLocationViewModel internal constructor() : BaseViewModel() {

    lateinit var geoCoder: Geocoder
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var getLocationSuccess = MutableLiveData<Event<Boolean>>()

    val locationAddr = ObservableField<String>()

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun stopGPS() {
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

    fun startGPS() {
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
                    getLocation(locationResult.lastLocation)

                } else {
                    Log.d(this::class.java.simpleName, "Location information isn't available.")
                }
            }
        }

        try {
            // Step 1.5, Subscribe to location changes.
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

        } catch (unlikely: SecurityException) {
            Log.e(this::class.java.simpleName, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    private fun getLocation(location: Location) {
        try {
            val listAddress: List<Address> = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            if (listAddress.isNotEmpty()) {
                val placeAddress = listAddress[0].getAddressLine(0)
                val placeName = listAddress[0].featureName
                locationAddr.set("$placeAddress $placeName")
                latitude.value = location.latitude.toString()
                longitude.value = location.longitude.toString()
                if (!latitude.value.isNullOrEmpty() && !longitude.value.isNullOrEmpty()) {
                    getLocationSuccess.value = Event(true)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun reset() {
        latitude.value = ""
        longitude.value = ""
        getLocationSuccess.value = Event(false)

        locationAddr.set("")

    }
}
