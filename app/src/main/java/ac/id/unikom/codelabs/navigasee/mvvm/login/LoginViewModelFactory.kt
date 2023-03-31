package ac.id.unikom.codelabs.navigasee.mvvm.login

import ac.id.unikom.codelabs.navigasee.data.source.LoginRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(
        private val repository: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = LoginViewModel(repository) as T
}
