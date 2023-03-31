package ac.id.unikom.codelabs.navigasee.data.source.remote.waiting

import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingBody
import ac.id.unikom.codelabs.navigasee.data.model.waiting.WaitingResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface WaitingApiService {

    @POST("users/nearby")
    suspend fun waiting(
            @Header("token") token: String,
            @Body body: WaitingBody
    ): WaitingResponse?
}