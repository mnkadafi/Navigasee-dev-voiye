package ac.id.unikom.codelabs.navigasee.mvvm.list_transportation_available

import ac.id.unikom.codelabs.navigasee.databinding.ListItemTransportationAvailableBinding
import ac.id.unikom.codelabs.navigasee.mvvm.sobat_waiting.SobatWaitingActivitiy
import ac.id.unikom.codelabs.navigasee.utilities.helper.Preferences
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class ListTransportationAvailableAdapter(val mViewModel: ListTransportationAvailableViewModel) : ListAdapter<ListTransportationAvailableModel, ListTransportationAvailableAdapter.ViewHolder>(DataDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            itemView.tag = item
            bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemTransportationAvailableBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), parent.context, mViewModel)
    }

    class ViewHolder(
            private val binding: ListItemTransportationAvailableBinding,
            private val context: Context,
            private val mViewModel: ListTransportationAvailableViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListTransportationAvailableModel) {
            binding.apply {
                mData = item
                mListener = object : ListTransportationAvailableUserActionListener {
                    override fun onClick(angkot_pertama: String) {
                        val intent = Intent(context, SobatWaitingActivitiy::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        val preferences = Preferences.getInstance()
                        preferences.setLangkahMenujuDestinasi(Gson().toJson(item.steps))
                        preferences.setTempatTujuan(mViewModel.destination.get())
                        preferences.setMenitPerjalanan(item.duration_jam_menit)
                        preferences.setLatTujuan(mViewModel.lat)
                        preferences.setLongTujuan(mViewModel.long)
                        preferences.setAngkutanPertama(angkot_pertama)
                        preferences.setTujuan(mViewModel.destination.get()!!)

//                        println(Gson().toJson(item.steps))

                        context.startActivity(intent)
                    }
                }
                executePendingBindings()
            }
        }
    }
}

private class DataDiffCallback : DiffUtil.ItemCallback<ListTransportationAvailableModel>() {

    override fun areItemsTheSame(oldItem: ListTransportationAvailableModel, newItem: ListTransportationAvailableModel): Boolean {
        return oldItem.nama_angkot == newItem.nama_angkot
    }

    override fun areContentsTheSame(oldItem: ListTransportationAvailableModel, newItem: ListTransportationAvailableModel): Boolean {
        return oldItem == newItem
    }
}