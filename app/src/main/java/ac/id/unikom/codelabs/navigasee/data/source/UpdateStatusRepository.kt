package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository

class UpdateStatusRepository : BaseRepository() {

    private val TAG by lazy { UpdateStatusRepository::class.java.simpleName }

    suspend fun updateStatus(aktif: Int, longitude: Double, latitude: Double): BaseApiResponse<String>? {
        if (token != null) {
            try {
                val response = ApiService.updateStatusApiService.updateStatus(token, aktif, longitude, latitude)
                if (response.status == 200) {
                    return response
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
        private var instance: UpdateStatusRepository? = null

        fun getInstance() = instance ?: UpdateStatusRepository().also { instance = it }
    }

}
