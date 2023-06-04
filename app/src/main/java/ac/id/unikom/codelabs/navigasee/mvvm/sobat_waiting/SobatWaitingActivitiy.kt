package ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivitySobatWaitingActivitiyBinding
import ac.id.unikom.codelabs.navigasee.mvvm.my_location.MyLocationActivity
import ac.id.unikom.codelabs.navigasee.mvvm.search_location.SearchLocationActivity
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import pub.devrel.easypermissions.EasyPermissions

class SobatWaitingActivitiy : BaseActivity<SobatWaitingViewModel, ActivitySobatWaitingActivitiyBinding>(R.layout.activity_sobat_waiting_activitiy), SobatWaitingActionListener {
    private val PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100
    private lateinit var preferences: Preferences

    companion object {
        const val ANGKUTAN_PERTAMA = "ANGKUTAN_PERTAMA"
        const val TUJUAN = "TUJUAN"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel
    }

    override fun onStart() {
        super.onStart()
        preferences = Preferences.getInstance()
        val tujuan = preferences.getTujuan()
        val angkutanPertama = preferences.getAngkutanPertama()
        if (tujuan != "" && angkutanPertama != "") {
            viewModel.angkutan_pertama = angkutanPertama ?: ""
            viewModel.tujuan = tujuan ?: ""
        } else {
            finish()
        }
        viewModel.help()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onCreateObserver(viewModel: SobatWaitingViewModel) {
        viewModel.batalkan.observe(this, EventObserver {
            if (it) {
                preferences.setWaiting("")
                val intent = Intent(applicationContext, MyLocationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        })
    }

    override fun setContentData() {
        preferences = Preferences.getInstance()
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST


    private val TAG by lazy { SearchLocationActivity::class.java.simpleName }

    private val viewModel: SobatWaitingViewModel by viewModels {
        SobatWaitingViewModelFactory(WaitingRepository.getInstance())
    }

}
