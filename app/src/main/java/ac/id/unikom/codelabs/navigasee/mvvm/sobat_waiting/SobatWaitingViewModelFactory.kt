package ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting

import ac.id.unikom.codelabs.navigasee.data.source.WaitingRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class SobatWaitingViewModelFactory(
        private val repository: WaitingRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = SobatWaitingViewModel(repository) as T
}