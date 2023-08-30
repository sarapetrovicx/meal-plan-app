package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.databinding.LayoutItemSavedMealBinding
import rs.raf.rmaprojekat.presentation.view.recycler.viewholder.SavedMealViewHolder
import java.util.function.Consumer

class SavedMealAdapter(
    private val diffCallback: DiffUtil.ItemCallback<SavedMeal>,
    private val onItemClicked: Consumer<SavedMeal>,
    private val onEditClicked: Consumer<SavedMeal>,
    private val onDeleteClicked: Consumer<SavedMeal>,
) : ListAdapter<SavedMeal, SavedMealViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMealViewHolder {
        val itemBinding = LayoutItemSavedMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedMealViewHolder(itemBinding, onItemClicked, onEditClicked, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: SavedMealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}