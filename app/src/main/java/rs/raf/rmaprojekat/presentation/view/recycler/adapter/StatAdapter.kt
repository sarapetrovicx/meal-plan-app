package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import rs.raf.rmaprojekat.data.models.saved.DisplayableItem
import rs.raf.rmaprojekat.databinding.LayoutFavoriteItemBinding


class StatAdapter<T : DisplayableItem>(
    private val diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, StatAdapter<T>.CategoryViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = LayoutFavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(
        private val itemBinding: LayoutFavoriteItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: DisplayableItem) {
            itemBinding.item.text = item.getDisplayText()
            itemBinding.count.text = item.getDisplayCount()

        }

    }
}
