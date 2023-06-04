package ac.id.unikom.codelabs.navigasee.mvvm.search_location

import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchLocationViewModelFactory(private val repository: SearchRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = SearchLocationViewModel(repository) as T
}
