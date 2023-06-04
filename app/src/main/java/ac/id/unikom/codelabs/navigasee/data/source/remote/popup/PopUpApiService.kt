package ac.id.unikom.codelabs.navigasee.data.source.remote.popup

import ac.id.unikom.codelabs.navigasee.data.model.popup.AntarKeTujuanResponse
import ac.id.unikom.codelabs.navigasee.data.model.popup.DatangiLangsungResponse
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface PopUpApiService {
    @POST("notification/help/accepted")
    @FormUrlEncoded
    suspend fun saya_akan_datangi_langsung(
            @Header("token") token: String,
            @Field("fcmPemohon") fcmPemohon: String,
            @Field("emailPenerima") emailPenerima: String
    ): DatangiLangsungResponse?

    @POST("users/finish")
    @FormUrlEncoded
    suspend fun akhiri_sesi(
            @Header("token") token: String,
            @Field("pemberiBantuan") emailPemberiBantuan: String,
            @Field("poin") poin: Int
    ): BaseApiResponse<DatangiLangsungResponse?>

    @POST("notification/help/accepted/antar")
    @FormUrlEncoded
    suspend fun saya_akan_bantu_sampai_tujuan(
            @Header("token") token: String,
            @Field("fcmPemohon") fcmPemohon: String,
            @Field("emailPenerima") emailPenerima: String,
            @Field("long") long: Double,
            @Field("lat") lat: Double
    ): AntarKeTujuanResponse?
}