package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rs.raf.rmaprojekat.R
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.databinding.LayoutItemAreaBinding
import rs.raf.rmaprojekat.presentation.view.recycler.viewholder.AreaViewHolder
import rs.raf.rmaprojekat.presentation.viewmodel.AreaViewModel
import java.util.function.Consumer

class AreaAdapter(
    private val diffCallback: DiffUtil.ItemCallback<Area>,
    private val onItemClicked: Consumer<Area>
) : ListAdapter<Area, AreaViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        val itemBinding = LayoutItemAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = itemBinding.root.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()

        return AreaViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}
