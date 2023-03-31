package ac.id.unikom.codelabs.navigasee.data.model.popup

import com.google.gson.annotations.SerializedName

data class DatangiLangsungResponse(

        @field:SerializedName("status")
        val status: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("namaPenerima")
        val namaPenerima: String? = null,

        @field:SerializedName("tipePenerima")
        val tipePenerima: String? = null,

        @field:SerializedName("jenisBantuan")
        val jenisBantuan: String? = null
)