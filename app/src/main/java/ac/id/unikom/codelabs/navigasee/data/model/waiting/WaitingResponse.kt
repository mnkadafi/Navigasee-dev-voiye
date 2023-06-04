package ac.id.unikom.codelabs.navigasee.data.model.waiting

import com.google.gson.annotations.SerializedName

data class WaitingResponse(

        @field:SerializedName("status")
        val status: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("namaPemohon")
        val namaPemohon: String? = null,

        @field:SerializedName("emailPemohon")
        val emailPemohon: String? = null,

        @field:SerializedName("tujuan")
        val tujuan: String? = null,

        @field:SerializedName("angkot")
        val angkot: String? = null,

        @field:SerializedName("penolak")
        val penolak: List<String?>? = null
)