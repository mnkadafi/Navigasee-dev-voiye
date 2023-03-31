package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class StartLocation(

        @field:SerializedName("lat")
        val lat: Any? = null,

        @field:SerializedName("lng")
        val lng: Any? = null
)