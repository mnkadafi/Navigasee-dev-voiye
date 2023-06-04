package ac.id.unikom.codelabs.navigasee.data.source.remote.search

import ac.id.unikom.codelabs.navigasee.data.model.search.SearchBody
import ac.id.unikom.codelabs.navigasee.data.model.search.SearchPlaceItem
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SearchApiService {

    @POST("routes/search")
    suspend fun search(
            @Header("token") token: String,
            @Body searchBody: SearchBody
    ): BaseApiResponse<List<SearchPlaceItem>>

}