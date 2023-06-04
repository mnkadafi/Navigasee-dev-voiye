package ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available

import com.google.gson.annotations.SerializedName

data class Duration(

        @field:SerializedName("text")
        val text: String? = null,

        @field:SerializedName("value")
        val value: Int? = null
)