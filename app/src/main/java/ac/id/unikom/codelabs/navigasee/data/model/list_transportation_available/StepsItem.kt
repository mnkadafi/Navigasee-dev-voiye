package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class StepsItem(

        @field:SerializedName("distance")
        val distance: Distance? = null,

        @field:SerializedName("duration")
        val duration: Duration? = null,

        @field:SerializedName("end_location")
        val endLocation: EndLocation? = null,

        @field:SerializedName("html_instructions")
        val htmlInstructions: String? = null,

        @field:SerializedName("maneuver")
        val maneuver: String? = null,

        @field:SerializedName("polyline")
        val polyline: Polyline? = null,

        @field:SerializedName("start_location")
        val startLocation: StartLocation? = null,

        @field:SerializedName("steps")
        val steps: List<StepsItem?>? = null,

        @field:SerializedName("transit_details")
        val transitDetails: TransitDetails? = null,

        @field:SerializedName("travel_mode")
        val travelMode: String? = null
)