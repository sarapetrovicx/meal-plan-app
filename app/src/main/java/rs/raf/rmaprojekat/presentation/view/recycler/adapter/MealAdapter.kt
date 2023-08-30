package rs.raf.rmaprojekat.presentation.view.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.databinding.LayoutItemMealBinding
import rs.raf.rmaprojekat.presentation.view.recycler.viewholder.MealViewHolder
import java.util.function.Consumer

class MealAdapter(
    private val diffCallback: DiffUtil.ItemCallback<Meal>,
    private val onItemClicked: Consumer<Meal>
) : ListAdapter<Meal, MealViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val itemBinding = LayoutItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(itemBinding, onItemClicked)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}