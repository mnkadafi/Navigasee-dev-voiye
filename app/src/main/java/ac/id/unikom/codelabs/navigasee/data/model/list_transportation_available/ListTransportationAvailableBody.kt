package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class ListTransportationAvailableBody(
        @field:SerializedName("startLong")
        val startLong: Double,

        @field:SerializedName("startLat")
        val startLat: Double,

        @field:SerializedName("destination_id")
        val destinationId: String
)