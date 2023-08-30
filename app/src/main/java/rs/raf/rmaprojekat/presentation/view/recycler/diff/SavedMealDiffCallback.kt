package rs.raf.rmaprojekat.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.rmaprojekat.data.models.saved.SavedMeal

class SavedMealDiffCallback : DiffUtil.ItemCallback<SavedMeal>() {

    override fun areItemsTheSame(oldItem: SavedMeal, newItem: SavedMeal): Boolean {
        return oldItem.idMeal == newItem.idMeal
    }

    override fun areContentsTheSame(oldItem: SavedMeal, newItem: SavedMeal): Boolean {
        return oldItem.date == newItem.date && oldItem.type == newItem.type
    }

}