package ac.id.unikom.codelabs.navigasee.data.source.remote.dashboard

import ac.id.unikom.codelabs.navigasee.data.model.dashboard.DashboardResponseData
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.*

interface DashboardApiService {

    @GET("users/profile")
    suspend fun getProfile(
            @Header("token") token: String
    ): BaseApiResponse<DashboardResponseData>

    @POST("notification/sendvcalltoken")
    @FormUrlEncoded
    suspend fun sendToken(
            @Header("token") token: String,
            @Field("fcmPenerima") fcm: String,
            @Field("tokenVC") tokenVc: String
    ): BaseApiResponse<DashboardResponseData>
}