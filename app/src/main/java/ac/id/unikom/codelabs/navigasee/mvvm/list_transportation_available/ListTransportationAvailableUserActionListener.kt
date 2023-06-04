package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.utilities.base.BaseUserActionListener

interface ListTransportationAvailableUserActionListener : BaseUserActionListener {
    fun onClick(angkot_pertama: String)
}