package rs.raf.rmaprojekat.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.models.category.Category
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient

class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {

    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.idIngredient == newItem.idIngredient
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.strIngredient == newItem.strIngredient
    }

}