package ac.id.unikom.codelabs.navigasee.utilities.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
        @SerializedName("status") var status: Int? = null,
        @SerializedName("message") var message: String? = null,
        @SerializedName("namaPemohon") var namaPemohon: String? = null,
        @SerializedName("emailPemohon") var emailPemohon: String? = null,
        @SerializedName("tujuan") var tujuan: String? = null,
        @SerializedName("angkot") var angkot: String? = null,
        @SerializedName("penolak") var penolak: List<String>? = null,
        @SerializedName("token") var token: String? = null,
        @SerializedName("data") @Expose var data: T? = null
)