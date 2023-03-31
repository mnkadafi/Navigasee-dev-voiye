package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat

import ac.id.unikom.codelabs.navigasee.MyApplication
import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityDashboardSupirSobatBinding
import ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat.pilih_hadiah.PilihHadiahActivity
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels

class DashboardSupirSobatActivity : BaseActivity<DashboardViewModel, ActivityDashboardSupirSobatBinding>(R.layout.activity_dashboard_supir_sobat), DashboardUserActionListener {
    override fun tukarPoin() {
        startActivity(Intent(this, PilihHadiahActivity::class.java))
    }

    private val PERMISSIONS_REQUEST_READ_FINE_LOCATION = 100
    private val viewModel: DashboardViewModel by viewModels {
        DashboardViewModelFactory(DashboardRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel

        MyApplication.instance.startService()
    }

    override fun onCreateObserver(viewModel: DashboardViewModel) {
        viewModel.apply {
            level.observe(this@DashboardSupirSobatActivity, EventObserver {
                mBinding.level.text = "$it"
            })

            poin_ago.observe(this@DashboardSupirSobatActivity, EventObserver {
                mBinding.progressBar.progress = Math.abs(it - 200)
            })
        }
    }

    override fun setContentData() {
        viewModel.preferences = Preferences.getInstance()
        viewModel.loadData()
        mBinding.swipeRefresh.setOnRefreshListener {
            mBinding.swipeRefresh.isRefreshing = false
            viewModel.loadData()
        }
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST
}
