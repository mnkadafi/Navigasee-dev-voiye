package ac.id.unikom.codelabs.navigasee.mvvm.search_location

import ac.id.unikom.codelabs.navigasee.data.source.SearchRepository
import ac.id.unikom.codelabs.navigasee.utilities.base.BaseViewModel
import androidx.databinding.ObservableField

class SearchLocationViewModel internal constructor(private val mRepository: SearchRepository) : BaseViewModel() {
    val cari_lokasi = ObservableField<String>()


}
