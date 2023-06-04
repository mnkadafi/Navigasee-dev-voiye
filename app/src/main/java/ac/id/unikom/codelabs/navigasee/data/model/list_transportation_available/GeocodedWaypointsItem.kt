package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class GeocodedWaypointsItem(

        @field:SerializedName("geocoder_status")
        val geocoderStatus: String? = null,

        @field:SerializedName("destination_id")
        val placeId: String? = null,

        @field:SerializedName("types")
        val types: List<String?>? = null
)