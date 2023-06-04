package ac.id.unikom.codelabs.navigasee.mvvm.list_search_location

import ac.id.unikom.codelabs.navigasee.data.model.search.SearchPlaceItem
import ac.id.unikom.codelabs.navigasee.databinding.ItemTujuanBinding
import ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available.ListTransportationAvailableActivity
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class ListSearchAdapter : ListAdapter<SearchPlaceItem, ListSearchAdapter.ViewHolder>(DataDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTujuanBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), parent.context)
    }

    class ViewHolder(
            private val binding: ItemTujuanBinding,
            private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchPlaceItem) {
            binding.apply {
                mData = item
                mListener = object : ListSearchActionListener {
                    override fun onclick() {
                        val i = Intent(context, ListTransportationAvailableActivity::class.java)
                        i.putExtra(ListTransportationAvailableActivity.DESTINATION_ID, item.placeId)
                        i.putExtra(ListTransportationAvailableActivity.DESTINATION, item.name)

                        val preferences = Preferences.getInstance()
                        preferences.setJarakKeDestinasi(item.distance!!)

                        context.startActivity(i)
                    }
                }
                executePendingBindings()
            }
        }
    }
}

private class DataDiffCallback : DiffUtil.ItemCallback<SearchPlaceItem>() {

    override fun areItemsTheSame(oldItem: SearchPlaceItem, newItem: SearchPlaceItem): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: SearchPlaceItem, newItem: SearchPlaceItem): Boolean {
        return oldItem == newItem
    }
}