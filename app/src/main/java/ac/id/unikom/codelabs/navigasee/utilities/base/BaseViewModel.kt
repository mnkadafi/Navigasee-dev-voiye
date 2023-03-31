package ac.id.unikom.codelabs.navigasee.utilities.base

import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    val isRequesting = MutableLiveData<Event<Boolean>>()
    val showMessage = MutableLiveData<Event<String>>()
    val showMessageRes = MutableLiveData<Event<Int>>()
}