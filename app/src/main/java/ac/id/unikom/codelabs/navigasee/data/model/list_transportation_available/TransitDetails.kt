package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class TransitDetails(

        @field:SerializedName("arrival_stop")
        val arrivalStop: ArrivalStop? = null,

        @field:SerializedName("arrival_time")
        val arrivalTime: ArrivalTime? = null,

        @field:SerializedName("departure_stop")
        val departureStop: DepartureStop? = null,

        @field:SerializedName("departure_time")
        val departureTime: DepartureTime? = null,

        @field:SerializedName("headsign")
        val headsign: String? = null,

        @field:SerializedName("headway")
        val headway: Int? = null,

        @field:SerializedName("line")
        val line: Line? = null,

        @field:SerializedName("num_stops")
        val numStops: Int? = null
)