package ac.id.unikom.codelabs.navigasee.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponseData(

        @field:SerializedName("email")
        val email: String? = null,

        @field:SerializedName("nama")
        val nama: String? = null,

        @field:SerializedName("tipe")
        val tipe: String? = null,

        @field:SerializedName("foto")
        val foto: String? = null,

        @field:SerializedName("poin")
        val poin: Int? = null,

        @field:SerializedName("total_poin")
        val totalPoin: Int = 0,

        @field:SerializedName("total_bantu")
        val totalBantu: Int = 0,

        @field:SerializedName("level")
        var level: Int = 0
)