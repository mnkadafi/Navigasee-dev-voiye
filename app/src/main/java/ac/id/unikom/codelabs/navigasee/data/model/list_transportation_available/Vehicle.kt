package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class Vehicle(

        @field:SerializedName("icon")
        val icon: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("type")
        val type: String? = null
)