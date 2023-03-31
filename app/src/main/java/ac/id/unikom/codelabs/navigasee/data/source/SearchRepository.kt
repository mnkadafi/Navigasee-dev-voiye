package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.search.SearchBody
import ac.id.unikom.codelabs.navigasee.data.model.search.SearchPlaceItem
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository
import android.util.Log

class SearchRepository : BaseRepository() {

    private val TAG by lazy { SearchRepository::class.java.simpleName }

    suspend fun search(startLong: Double, startLat: Double, query: String): List<SearchPlaceItem>? {
        if (token != null) {
            var data: List<SearchPlaceItem>? = null
            try {
                val response = ApiService.searchApiService.search(token, SearchBody(startLong, startLat, query))
                if (response.status == 200) {
                    data = response.data
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
            return data
        } else {
            return null
        }

    }

    companion object {
        // For Singleton instantiation
        private var instance: SearchRepository? = null

        fun getInstance() = instance ?: SearchRepository().also { instance = it }
    }

}
