package ac.id.unikom.codelabs.navigasee.webrtcnew.kelas

import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CallViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CallViewModel(repository) as T
}