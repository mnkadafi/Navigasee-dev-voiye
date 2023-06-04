package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class Bounds(

        @field:SerializedName("northeast")
        val northeast: Northeast? = null,

        @field:SerializedName("southwest")
        val southwest: Southwest? = null
)