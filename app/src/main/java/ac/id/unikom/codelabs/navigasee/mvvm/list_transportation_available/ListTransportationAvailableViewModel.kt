package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.StepsItem
import ac.id.unikom.codelabs.navigasee.data.source.ListTransportationAvailableRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import ac.id.unikom.codelabs.navigasee.utilities.helper.Event
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ListTransportationAvailableViewModel internal constructor(val mRepository: ListTransportationAvailableRepository) : BaseViewModel() {
    lateinit var destination_id: String
    var destination = ObservableField<String>()
    var start = ObservableField<String>()
    var long: String? = ""
    var lat: String? = ""

    var listLiveData = MutableLiveData<List<ListTransportationAvailableModel>>()

    fun loadData(longitude: Double, latitude: Double, isRefresh: Boolean = false) {
        lat = latitude.toString()
        long = longitude.toString()
        val list = ArrayList<ListTransportationAvailableModel>()
        if (listLiveData.value == null || isRefresh) {
            isRequesting.value = Event(true)
            viewModelScope.launch {
                val response = mRepository.getData(longitude, latitude, destination_id)
                if (response != null) {
                    try {
                        response.routes?.forEach {
                            it?.legs?.forEach {
                                val trayek: ArrayList<String> = ArrayList()
                                val steps: ArrayList<StepsItem?> = ArrayList()

                                it?.steps?.forEach {
                                    if (it?.travelMode.equals("TRANSIT")) {
                                        trayek.add(it?.transitDetails?.line?.name!!)
                                        steps.add(it)
                                    } else if (it?.travelMode.equals("WALKING")) {
                                        it?.steps?.forEach {
                                            steps.add(it)
                                        }
                                    }
                                }
                                val item = ListTransportationAvailableModel(trayek, steps, it?.duration!!.text)
                                list.add(item)
                            }
                        }
                        listLiveData.value = list.filter { !it.nama_angkot.isNullOrEmpty() }
                        list.removeAll(list)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
                isRequesting.value = Event(false)
            }
        }
    }
}

