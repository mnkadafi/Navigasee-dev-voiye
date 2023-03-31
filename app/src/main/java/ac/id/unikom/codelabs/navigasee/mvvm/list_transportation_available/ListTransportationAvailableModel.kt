package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.StepsItem

data class ListTransportationAvailableModel(
        var nama_angkot: List<String>? = null,
        var steps: List<StepsItem?>? = null,
        var duration_jam_menit: String? = null
)