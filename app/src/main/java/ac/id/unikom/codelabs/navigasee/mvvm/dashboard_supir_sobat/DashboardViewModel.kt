package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat

import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DashboardViewModel internal constructor(var mRepository: DashboardRepository) : BaseViewModel() {

    var preferences: Preferences = Preferences.getInstance()
    val nama = ObservableField<String>()
    val keterangan_poin = ObservableField<String>()
    val level = MutableLiveData<Event<Int>>()
    val pointTerkumpul = ObservableInt()
    val poin_ago = MutableLiveData<Event<Int>>()
    val tunanetraTerbantukan = ObservableInt()
    var photoProfile = ObservableField<String>()

    fun loadData() {
        isRequesting.value = Event(true)
        viewModelScope.launch {
            val data = mRepository.getProfile()
            if (data != null) {
                if (data.level == 0) {
                    data.level = 1
                }
                nama.set(data.nama)
                level.value = Event(data.level)
                pointTerkumpul.set(data.totalPoin)
                tunanetraTerbantukan.set(data.totalBantu)
                photoProfile.set(data.foto)
                val nextLevel = data.level + 1
                val mPoinAgo = data.level * 200 - data.totalPoin
                poin_ago.value = Event(mPoinAgo)
                keterangan_poin.set("${mPoinAgo} points to level $nextLevel .")
            } else {
                showMessage.value = Event("Data empty!")
            }
            isRequesting.value = Event(false)
        }
    }
}

