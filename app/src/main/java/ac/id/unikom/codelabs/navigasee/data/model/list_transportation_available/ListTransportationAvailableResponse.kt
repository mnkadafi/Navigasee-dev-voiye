package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class ListTransportationAvailableResponse(

        @field:SerializedName("geocoded_waypoints")
        val geocodedWaypoints: List<GeocodedWaypointsItem?>? = null,

        @field:SerializedName("routes")
        val routes: List<RoutesItem?>? = null,

        @field:SerializedName("status")
        val status: String? = null
)