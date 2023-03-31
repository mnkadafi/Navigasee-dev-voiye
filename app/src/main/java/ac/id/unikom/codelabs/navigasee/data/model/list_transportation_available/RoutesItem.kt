package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class RoutesItem(

        @field:SerializedName("bounds")
        val bounds: Bounds? = null,

        @field:SerializedName("copyrights")
        val copyrights: String? = null,

        @field:SerializedName("fare")
        val fare: Fare? = null,

        @field:SerializedName("legs")
        val legs: List<LegsItem?>? = null,

        @field:SerializedName("overview_polyline")
        val overviewPolyline: OverviewPolyline? = null,

        @field:SerializedName("summary")
        val summary: String? = null,

        @field:SerializedName("warnings")
        val warnings: List<String?>? = null,

        @field:SerializedName("waypoint_order")
        val waypointOrder: List<Any?>? = null
)