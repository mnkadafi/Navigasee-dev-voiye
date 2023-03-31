package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class LegsItem(

        @field:SerializedName("arrival_time")
        val arrivalTime: ArrivalTime? = null,

        @field:SerializedName("departure_time")
        val departureTime: DepartureTime? = null,

        @field:SerializedName("distance")
        val distance: Distance? = null,

        @field:SerializedName("duration")
        val duration: Duration? = null,

        @field:SerializedName("end_address")
        val endAddress: String? = null,

        @field:SerializedName("end_location")
        val endLocation: EndLocation? = null,

        @field:SerializedName("start_address")
        val startAddress: String? = null,

        @field:SerializedName("start_location")
        val startLocation: StartLocation? = null,

        @field:SerializedName("steps")
        val steps: List<StepsItem?>? = null,

        @field:SerializedName("traffic_speed_entry")
        val trafficSpeedEntry: List<Any?>? = null,

        @field:SerializedName("via_waypoint")
        val viaWaypoint: List<Any?>? = null
)