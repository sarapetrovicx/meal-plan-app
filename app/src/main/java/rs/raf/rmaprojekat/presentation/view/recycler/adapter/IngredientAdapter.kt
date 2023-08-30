package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.databinding.LayoutItemIngredientBinding
import rs.raf.rmaprojekat.presentation.view.recycler.viewholder.IngredientViewHolder
import java.util.function.Consumer

class IngredientAdapter(
    private val diffCallback: DiffUtil.ItemCallback<Ingredient>,
    private val onItemClicked: Consumer<Ingredient>
) : ListAdapter<Ingredient, IngredientViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemBinding = LayoutItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams = itemBinding.root.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()

        return IngredientViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}
