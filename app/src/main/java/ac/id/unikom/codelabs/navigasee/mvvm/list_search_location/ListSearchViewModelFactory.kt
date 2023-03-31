package ac.id.unikom.codelabs.navigasee.mvvm.list_search_location

import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListSearchViewModelFactory(
        private val repository: SearchRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ListSearchViewModel(repository) as T
}
