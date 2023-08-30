package rs.raf.rmaprojekat.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.rmaprojekat.data.models.category.Category

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.idCategory == newItem.idCategory
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.strCategory == newItem.strCategory
    }

}