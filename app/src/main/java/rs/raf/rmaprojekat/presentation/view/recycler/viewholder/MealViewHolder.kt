package rs.raf.rmaprojekat.presentation.view.recycler.viewholder

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.databinding.LayoutItemIngredientBinding
import rs.raf.rmaprojekat.databinding.LayoutItemMealBinding
import java.util.function.Consumer


class MealViewHolder(
    private val itemBinding: LayoutItemMealBinding,
    private val onItemClicked: Consumer<Meal>,
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(meal: Meal) {
        itemBinding.titleTv.text = meal.strMeal
        Glide.with(itemView)
            .load(meal.strMealThumb)
            .into(itemBinding.mealImageView)

        itemBinding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked.accept(meal)
            }

        }
    }

}