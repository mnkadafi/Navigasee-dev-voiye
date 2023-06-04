package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.ListTransportationAvailableBody
import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.ListTransportationAvailableResponse
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository

class ListTransportationAvailableRepository : BaseRepository() {

    private val TAG by lazy { ListTransportationAvailableRepository::class.java.simpleName }

    suspend fun getData(startLong: Double, startLat: Double, destinationId: String): ListTransportationAvailableResponse? {
        if (token != null) {
            try {
                val listTransportationAvailableBody = ListTransportationAvailableBody(startLong, startLat, destinationId)
                val response = ApiService
                        .listTransportationAvailableApiService
                        .getListTransportationAvailable(token, listTransportationAvailableBody)
                if (response.status == 200) {
                    return response.data
                } else {
                    return null
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                return null
            }
        } else {
            return null
        }
    }

    companion object {
        // For Singleton instantiation
        private var instance: ListTransportationAvailableRepository? = null

        fun getInstance() = instance
                ?: ListTransportationAvailableRepository().also { instance = it }
    }

}
