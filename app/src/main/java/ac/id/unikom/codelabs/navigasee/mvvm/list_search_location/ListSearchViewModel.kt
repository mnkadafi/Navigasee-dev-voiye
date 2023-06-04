package ac.id.unikom.codelabs.navigasee.mvvm.list_search_location

import ac.id.unikom.codelabs.navigasee.data.model.search.SearchPlaceItem
import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ListSearchViewModel internal constructor(private val mRepository: SearchRepository) : BaseViewModel() {

    val listDestination = MutableLiveData<List<SearchPlaceItem>>()

    fun searchLocation(longitude: Double,
                       latidude: Double,
                       tujuan: String,
                       isRefresh: Boolean = false) {
        if (listDestination.value == null || isRefresh) {
            isRequesting.value = Event(true)
            viewModelScope.launch {
                val location = mRepository.search(longitude, latidude, tujuan) as ArrayList<SearchPlaceItem>?
                if (location != null) {
                    val list = ArrayList<SearchPlaceItem>()
                    location.forEachIndexed { _, searchPlaceItem ->
                        val splitted = searchPlaceItem.distance!!.split(" ")
                        searchPlaceItem.distance = splitted[0]
                        list.add(searchPlaceItem)
                    }

                    location.removeAll(location)
                    listDestination.value = list.sortedBy { it.distance }
                } else {
                    showMessage.value = Event("Location not found!")
                }
                isRequesting.value = Event(false)
            }
        }
    }
}

