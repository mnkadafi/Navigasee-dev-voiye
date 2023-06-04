package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingResponse
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository

class WaitingRepository : BaseRepository() {

    private val TAG by lazy { WaitingRepository::class.java.simpleName }

    suspend fun waiting(body: WaitingBody): WaitingResponse? {
        if (token != null) {
            try {
                val response = ApiService.waitingApiService.waiting(token, body)
                return response
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
        private var instance: WaitingRepository? = null

        fun getInstance() = instance ?: WaitingRepository().also { instance = it }
    }

}
