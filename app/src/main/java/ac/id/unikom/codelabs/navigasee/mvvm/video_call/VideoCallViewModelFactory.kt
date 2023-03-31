package ac.id.unikom.codelabs.navigasee.mvvm.video_call

import ac.id.unikom.codelabs.navigasee.data.source.DashboardRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VideoCallViewModelFactory(
        private val repository: DashboardRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = VideoCallViewModel(repository) as T
}