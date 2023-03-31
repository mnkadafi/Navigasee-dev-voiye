package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class AgenciesItem(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("phone")
        val phone: String? = null,

        @field:SerializedName("url")
        val url: String? = null
)