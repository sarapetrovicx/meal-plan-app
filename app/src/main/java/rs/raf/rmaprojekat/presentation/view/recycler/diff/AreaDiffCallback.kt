package rs.raf.rmaprojekat.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.models.category.Category

class AreaDiffCallback : DiffUtil.ItemCallback<Area>() {

    override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
        return oldItem.idArea == newItem.idArea
    }

    override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
        return oldItem.strArea == newItem.strArea
    }

}