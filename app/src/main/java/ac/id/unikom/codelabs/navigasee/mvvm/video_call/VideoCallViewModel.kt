package ac.id.unikom.codelabs.navigasee.mvvm.video_call

import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VideoCallViewModel(private val mRepository: DashboardRepository) : BaseViewModel() {
    var tokenSent = MutableLiveData<Event<Boolean>>()

    fun sendToken(fcmPenerima: String, tokenVC: String) {
        viewModelScope.launch {
            val response = mRepository.sendToken(fcmPenerima, tokenVC)
            if (response) tokenSent.value = Event(true)
            else showMessage.value = Event("An error occurred while sending the Video Call token")
        }
    }

    fun updateSendToken() {
        tokenSent.value = Event(true)
    }
}