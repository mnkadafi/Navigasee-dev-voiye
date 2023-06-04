package ac.id.unikom.codelabs.navigasee.data.source.remote.update_status

import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface UpdateStatusApiService {

    @POST("users/status/update")
    @FormUrlEncoded
    suspend fun updateStatus(
            @Header("token") token: String,
            @Field("aktif") aktif: Int,
            @Field("latitude") latitude: Double,
            @Field("longitude") longitude: Double
    ): BaseApiResponse<String>

}