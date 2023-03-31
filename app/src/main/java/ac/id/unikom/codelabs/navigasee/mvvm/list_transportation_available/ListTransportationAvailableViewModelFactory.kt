package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.data.source.ListTransportationAvailableRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListTransportationAvailableViewModelFactory(
        private val repository: ListTransportationAvailableRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ListTransportationAvailableViewModel(repository) as T
}
