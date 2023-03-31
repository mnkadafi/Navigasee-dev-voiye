package ac.id.unikom.codelabs.navigasee.data.source.remote.list_transportation_available

import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.ListTransportationAvailableBody
import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.ListTransportationAvailableResponse
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ListTransportationAvailableApiService {

    @POST("routes/angkot")
    suspend fun getListTransportationAvailable(
            @Header("token") token: String,
            @Body body: ListTransportationAvailableBody
    ): BaseApiResponse<ListTransportationAvailableResponse>

}