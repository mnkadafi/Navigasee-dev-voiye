package ac.id.unikom.codelabs.navigasee.data.model.search

import com.google.gson.annotations.SerializedName

data class SearchPlaceItem(

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("distance")
        var distance: String? = null,

        @field:SerializedName("place_id")
        val placeId: String? = null
)