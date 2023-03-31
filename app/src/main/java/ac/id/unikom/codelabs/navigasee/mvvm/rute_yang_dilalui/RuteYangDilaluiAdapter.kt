package ac.id.unikom.codelabs.navigasee.mvvm.rute_yang_dilalui

import ac.id.unikom.codelabs.navigasee.data.model.list_transportation_available.StepsItem
import ac.id.unikom.codelabs.navigasee.databinding.ListItemRuteYangDilaluiBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class RuteYangDilaluiAdapter : ListAdapter<StepsItem, RuteYangDilaluiAdapter.ViewHolder>(DataDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            itemView.tag = item
            bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemRuteYangDilaluiBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(
            private val binding: ListItemRuteYangDilaluiBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StepsItem) {
            binding.apply {
                mData = item
                executePendingBindings()
            }
        }
    }
}

private class DataDiffCallback : DiffUtil.ItemCallback<StepsItem>() {

    override fun areItemsTheSame(oldItem: StepsItem, newItem: StepsItem): Boolean {
        return oldItem.travelMode == newItem.travelMode
    }

    override fun areContentsTheSame(oldItem: StepsItem, newItem: StepsItem): Boolean {
        return oldItem == newItem
    }
}