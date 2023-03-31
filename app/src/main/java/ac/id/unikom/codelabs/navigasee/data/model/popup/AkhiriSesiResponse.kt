package ac.id.unikom.codelabs.navigasee.data.model.popup

import com.google.gson.annotations.SerializedName

data class AkhiriSesiResponse(

        @field:SerializedName("id_transaksi")
        val idTransaksi: Int? = null,

        @field:SerializedName("jumlah_point")
        val jumlahPoint: Int? = null,

        @field:SerializedName("tanggal")
        val tanggal: String? = null,

        @field:SerializedName("pemberi")
        val pemberi: String? = null,

        @field:SerializedName("penerima")
        val penerima: String? = null,

        @field:SerializedName("poin")
        val poin: String? = null
)