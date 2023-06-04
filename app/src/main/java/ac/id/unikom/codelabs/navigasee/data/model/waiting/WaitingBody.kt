package ac.id.unikom.codelabs.navigasee.data.model.waiting

data class WaitingBody(
        val pemohon: String,
        val tujuan: String,
        val angkot: String,
        val latitude: Double?,
        val longitude: Double?,
        val penolak: ArrayList<String>?
)