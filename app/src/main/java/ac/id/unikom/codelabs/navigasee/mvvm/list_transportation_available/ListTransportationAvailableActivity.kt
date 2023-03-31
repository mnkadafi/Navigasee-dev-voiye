package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.ListTransportationAvailableRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityListTransportationAvailableBinding
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import pub.devrel.easypermissions.EasyPermissions

class ListTransportationAvailableActivity : BaseActivity<ListTransportationAvailableViewModel, ActivityListTransportationAvailableBinding>(R.layout.activity_list_transportation_available) {
    companion object {
        const val DESTINATION_ID = "destination_id"
        const val DESTINATION = "destination"
    }

    private lateinit var adapter: ListTransportationAvailableAdapter
    private val viewModel: ListTransportationAvailableViewModel by viewModels {
        ListTransportationAvailableViewModelFactory(ListTransportationAvailableRepository.getInstance())
    }
    private val TAG = this::class.java.simpleName
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    override fun onCreateObserver(viewModel: ListTransportationAvailableViewModel) {
        viewModel.isRequesting.removeObservers(this)
        viewModel.listLiveData.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                adapter.submitList(it)
            } else {
                viewModel.showMessageRes.value = Event(R.string.tidak_ada_kendaraan_umum_tersedia)
            }
        })
        viewModel.isRequesting.observe(this, EventObserver {
            mBinding.swipeRefresh.isRefreshing = it
        })
    }

    override fun setContentData() {
        viewModel.destination_id = intent.getStringExtra(DESTINATION_ID)!!
        viewModel.destination.set(intent.getStringExtra(DESTINATION))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = viewModel.destination.get()
        supportActionBar!!.elevation = 0.0f

        val preferences = Preferences.getInstance()
        viewModel.start.set(preferences.getCurrentPlace())
        longitude = preferences.getLongitude()!!.toDouble()
        latitude = preferences.getLatitude()!!.toDouble()

        if (viewModel.destination_id.isEmpty()) {
            finish()
            Log.e(TAG, "destination_id is empty")
        }

        adapter = ListTransportationAvailableAdapter(viewModel)
        mBinding.recyclerView.adapter = adapter
        mBinding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData(longitude, latitude, true)
        }

        viewModel.loadData(longitude, latitude)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mViewModel = viewModel
        mParentVM = viewModel
        mProgressType = PROGRESS_TYPE_SWIPE

    }
}
