package ac.id.unikom.codelabs.navigasee.mvvm.my_location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyLocationViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MyLocationViewModel() as T
}
