package ac.id.unikom.codelabs.navigasee.mvvm.search_location

import ac.id.unikom.codelabs.navigasee.R
import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import ac.id.unikom.codelabs.navigasee.databinding.ActivitySearchLocationBinding
import ac.id.unikom.codelabs.navigasee.mvvm.list_search_location.ListSearchActivity
import ac.id.unikom.codelabs.navigasee.utilities.TEMPAT_TUJUAN
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.KeyEvent
import androidx.activity.viewModels
import kotlinx.android.synthetic.main.activity_search_location.*
import java.util.*

class SearchLocationActivity : BaseActivity<SearchLocationViewModel, ActivitySearchLocationBinding>(R.layout.activity_search_location), SearchLocationActionListener {

//    TODO : retrieve google maps result and populate list, take a loot at garden.plant_list for recyclerview example

    private val TAG by lazy { SearchLocationActivity::class.java.simpleName }

    private var longitude: Double? = null
    private var latidude: Double? = null

    private val viewModel: SearchLocationViewModel by viewModels {
        SearchLocationViewModelFactory(SearchRepository.getInstance())
    }

    override fun onCreateObserver(viewModel: SearchLocationViewModel) {

    }

    override fun setContentData() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun setMessageType(): String = MESSAGE_TYPE_TOAST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mParentVM = viewModel

        editText.setOnKeyListener { _, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (i) {
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER -> {
                        nextPage()
                        return@setOnKeyListener false
                    }
                }
            }
            return@setOnKeyListener false
        }

        val preferences = Preferences.getInstance()
        Log.d("CEK", "LATITUDE: ${preferences.getLatitude()} LONGITUDE: ${preferences.getLatitude()}")

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun nextPage() {
        val intent = Intent(this, ListSearchActivity::class.java)
        intent.putExtra(TEMPAT_TUJUAN, viewModel.cari_lokasi.get())
        startActivity(intent)
    }

    companion object {
        const val REQ_CODE_SPEECH_INPUT = 1
    }

    override fun micOnClick() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Log.wtf(TAG, a.localizedMessage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    if (result != null) {
                        viewModel.cari_lokasi.set(result[0])
                    }
//                  langsung intent ke activity list of tempat lokasi dari google maps
                    nextPage()
                }
            }
        }
    }

}
