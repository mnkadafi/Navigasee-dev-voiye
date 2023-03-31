package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class DepartureStop(

        @field:SerializedName("location")
        val location: Location? = null,

        @field:SerializedName("name")
        val name: String? = null
)