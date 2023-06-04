package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class Line(

        @field:SerializedName("agencies")
        val agencies: List<AgenciesItem?>? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("vehicle")
        val vehicle: Vehicle? = null
)