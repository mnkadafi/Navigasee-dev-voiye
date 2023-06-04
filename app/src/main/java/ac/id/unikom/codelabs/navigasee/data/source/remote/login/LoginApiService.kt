package ac.id.unikom.codelabs.navigasee.data.source.remote.login

import ac.id.unikom.codelabs.navigasee.data.model.login.LoginResponse
import ac.id.unikom.codelabs.navigasee.data.model.login.User
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.*

interface LoginApiService {

    @POST("login")
    suspend fun login(
            @Body user: User
    ): LoginResponse

    @POST("users/fcm")
    @FormUrlEncoded
    suspend fun updateFcm(
            @Header("token") token: String,
            @Field("email") email: String,
            @Field("fcm_token") fcm_token: String
    ): BaseApiResponse<*>
}