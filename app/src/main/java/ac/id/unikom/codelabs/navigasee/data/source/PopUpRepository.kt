package ac.id.unikom.codelabs.navigasee.data.source

import ac.id.unikom.codelabs.navigasee.data.model.popup.AntarKeTujuanResponse
import ac.id.unikom.codelabs.navigasee.data.model.popup.DatangiLangsungResponse
import ac.id.unikom.codelabs.navigasee.data.source.remote.ApiService
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseRepository

class PopUpRepository : BaseRepository() {

    private val TAG by lazy { PopUpRepository::class.java.simpleName }

    suspend fun saya_akan_datangi_langsung(fcmPenerima: String, emailPenerima: String): DatangiLangsungResponse? {
        if (token != null) {
            try {
                val response = ApiService.popupApiService.saya_akan_datangi_langsung(token, fcmPenerima, emailPenerima)
                if (response?.status == 200) {
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

    suspend fun saya_akan_bantu_sampai_tujuan(fcmPenerima: String, emailPenerima: String, long: Double, lat: Double): AntarKeTujuanResponse? {
        if (token != null) {
            try {
                val response = ApiService.popupApiService.saya_akan_bantu_sampai_tujuan(
                        token, fcmPenerima, emailPenerima, long, lat
                )
                return response

            } catch (e: Throwable) {
                e.printStackTrace()
                return null
            }
        } else {
            return null
        }

    }

    suspend fun akhiri_sesi(emailPemberiBantuan: String, poin: Int): BaseApiResponse<DatangiLangsungResponse?>? {
        if (token != null) {
            try {
                val response = ApiService.popupApiService.akhiri_sesi(token, emailPemberiBantuan, poin)
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
        private var instance: PopUpRepository? = null

        fun getInstance() = instance ?: PopUpRepository().also { instance = it }
    }

}
