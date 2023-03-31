package ac.id.unikom.codelabs.navigasee.data.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

        @field:SerializedName("status")
        val status: Int? = null,

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("nama")
        val nama: String? = null,

        @field:SerializedName("tipe")
        val tipe: String? = null,

        @field:SerializedName("token")
        val token: String? = null
)