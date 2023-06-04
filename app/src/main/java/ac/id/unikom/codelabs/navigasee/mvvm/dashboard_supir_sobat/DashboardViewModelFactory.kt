package ac.id.unikom.codelabs.navigasee.mvvm.dashboard_supir_sobat

import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DashboardViewModelFactory(
        private val repository: DashboardRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = DashboardViewModel(repository) as T
}
