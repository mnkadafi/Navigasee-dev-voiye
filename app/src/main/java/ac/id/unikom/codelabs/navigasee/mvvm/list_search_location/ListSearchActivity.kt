package ac.id.unikom.codelabs.navigasee.mvvm.list_search_location

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivityListSearchBinding
import ac.id.unikom.codelabs.navigasee.utilities.TEMPAT_TUJUAN
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import ac.id.unikom.codelabs.navigasee.utilities.helper.EventObserver
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class ListSearchActivity : BaseActivity<ListSearchViewModel, ActivityListSearchBinding>(R.layout.activity_list_search),
        ListSearchActionListener {

    private lateinit var adapter: ListSearchAdapter

    override fun setContentData() {
        val preferences = Preferences.getInstance()

        viewModel.searchLocation(preferences.getLongitude()?.toDouble()!!, preferences.getLatitude()?.toDouble()!!, intent.getStringExtra(TEMPAT_TUJUAN)!!)

        adapter = ListSearchAdapter()
        mBinding.listSearchRecyclerview.adapter = adapter

        mBinding.swipeRefresh.setOnRefreshListener {
            viewModel.searchLocation(
                    preferences.getLongitude()?.toDouble()!!,
                    preferences.getLatitude()?.toDouble()!!,
                    intent.getStringExtra(TEMPAT_TUJUAN)!!,
                    true
            )
        }
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST

    private val viewModel: ListSearchViewModel by viewModels {
        ListSearchViewModelFactory(SearchRepository.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel
        mProgressType = PROGRESS_TYPE_SWIPE

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.getStringExtra(TEMPAT_TUJUAN)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onclick() {

    }

    override fun onCreateObserver(viewModel: ListSearchViewModel) {
        viewModel.isRequesting.removeObservers(this)
        viewModel.isRequesting.observe(this, EventObserver {
            mBinding.swipeRefresh.isRefreshing = it
        })

        viewModel.listDestination.observe(this, Observer {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
            } else {
                viewModel.showMessage.value = Event("Place not found!")
            }
        })
    }

}
