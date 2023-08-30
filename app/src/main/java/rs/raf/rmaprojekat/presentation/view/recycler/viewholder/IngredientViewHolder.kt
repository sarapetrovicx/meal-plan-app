package rs.raf.rmaprojekat.presentation.view.recycler.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.databinding.LayoutItemIngredientBinding
import java.util.function.Consumer

class IngredientViewHolder(
    private val itemBinding: LayoutItemIngredientBinding,
    private val onItemClicked: Consumer<Ingredient>,
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(ingredient: Ingredient) {

        itemBinding.titleTv.text = ingredient.strIngredient

        itemBinding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked.accept(ingredient)
            }
        }
    }

    private fun showDescriptionDialog(context: Context, description: String, name: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(name)
            .setMessage(description)
            .setPositiveButton("OK", null)
            .create()
        dialog.show()
    }
}