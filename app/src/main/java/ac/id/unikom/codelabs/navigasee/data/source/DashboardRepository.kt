package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.dashboard.DashboardResponseData
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository

class DashboardRepository : BaseRepository() {

    private val TAG by lazy { DashboardRepository::class.java.simpleName }

    suspend fun getProfile(): DashboardResponseData? {
        if (token != null) {
            try {
                val response = ApiService.dashboardApiService.getProfile(token)
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

    suspend fun sendToken(fcmPenerima: String, tokenVC: String): Boolean {
        if (token != null) {
            try {
                val response = ApiService.dashboardApiService.sendToken(token, fcmPenerima, tokenVC)
                if (response.status == 200) return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        return true
    }

    companion object {
        // For Singleton instantiation
        private var instance: DashboardRepository? = null

        fun getInstance() = instance ?: DashboardRepository().also { instance = it }
    }

}
