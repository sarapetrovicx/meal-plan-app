package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rs.raf.rmaprojekat.data.models.category.Category
import rs.raf.rmaprojekat.databinding.LayoutItemCategoryBinding
import rs.raf.rmaprojekat.presentation.view.recycler.viewholder.CategoryViewHolder
import java.util.function.Consumer

class CategoryAdapter(
    private val diffCallback: DiffUtil.ItemCallback<Category>,
    private val onCategoryClicked: Consumer<Category>
) : ListAdapter<Category, CategoryViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemBinding = LayoutItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = itemBinding.root.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()

        return CategoryViewHolder(itemBinding, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}
