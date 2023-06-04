package ac.id.unikom.codelabs.navigasee.data.model.search

data class SearchBody(
        val startLong: Double,
        val startLat: Double,
        val query: String
)