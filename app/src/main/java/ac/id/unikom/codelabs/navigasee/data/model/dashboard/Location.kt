package ac.id.unikom.codelabs.navigasee.data.model.dashboard

import com.google.gson.annotations.SerializedName

data class Location(

        @field:SerializedName("latitude")
        val latitude: Double? = null,

        @field:SerializedName("longitude")
        val longitude: Double? = null
)