package ac.id.unikom.codelabs.navigasee.mvvm.my_location

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.databinding.ActivityMyLocationBinding
import ac.id.unikom.codelabs.navigasee.mvvm.login.LoginActivity
import ac.id.unikom.codelabs.navigasee.mvvm.search_location.SearchLocationActivity
import ac.id.unikom.codelabs.navigasee.utilities.LATITUDE
import ac.id.unikom.codelabs.navigasee.utilities.LONGITUDE
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.extensions.showToast
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_my_location.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(ac.id.unikom.codelabs.navigasee.R.layout.activity_my_location), MyLocationActionListener {
    private val TAG by lazy { LoginActivity::class.java.simpleName }

    private val PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100

    private val viewModel: MyLocationViewModel by viewModels {
        MyLocationViewModelFactory()
    }

    override fun nextPage() {
        val i = Intent(this, SearchLocationActivity::class.java)
        i.putExtra(LONGITUDE, viewModel.longitude.value)
        i.putExtra(LATITUDE, viewModel.latitude.value)

        viewModel.stopGPS()

        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel

        viewModel.geoCoder = Geocoder(this, Locale.getDefault())

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                        permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_READ_FINE_LOCATION)
        }

        supportActionBar?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onCreateObserver(viewModel: MyLocationViewModel) {
        viewModel.apply {
            getLocationSuccess.observe(this@MyLocationActivity, EventObserver {
                if (it) {
                    btn_mylocation_ok.isEnabled = true

                    //save to shared pref
                    val preferences = Preferences.getInstance()
                    preferences.saveLocation(viewModel.longitude.value, viewModel.latitude.value, viewModel.locationAddr.get())

                    // tell the user
                    tv_mylocation_current_location.requestFocus()
                } else {
                    btn_mylocation_ok.isEnabled = false

                    //set empty location shared pref
                    val preferences = Preferences.getInstance()
                    preferences.saveLocation("", "", "")
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reset()
    }

    override fun setContentData() {
        viewModel.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        viewModel.startGPS()

        showToast(R.string.menunggu_lokasi_gps)
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST

}
