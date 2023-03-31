package ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting

import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SobatWaitingViewModel internal constructor(private val mRepository: WaitingRepository) : BaseViewModel() {

    lateinit var body: WaitingBody
    var tujuan = ""
    var angkutan_pertama = ""
    var batalkan = MutableLiveData<Event<Boolean>>()
    var preferences = Preferences.getInstance()

    fun help() {
        if (!tujuan.isEmpty()) {
            body = WaitingBody(
                    preferences.getEmail()!!,
                    tujuan,
                    angkutan_pertama,
                    preferences.getLatitude()!!.toDouble(),
                    preferences.getLongitude()!!.toDouble(),
                    ArrayList()
            )

            preferences.setWaiting(Gson().toJson(body))

        } else if (!preferences.getWaiting().isNullOrEmpty()) {
            body = Gson().fromJson(preferences.getWaiting(), WaitingBody::class.java)
        }

        viewModelScope.launch {
            val response = mRepository.waiting(body)
            if (response == null) {
                showMessage.value =
                    Event("There's no voiye buddy or public transport driver active")
                batalkan.value = Event(true)
            }

        }
    }

}