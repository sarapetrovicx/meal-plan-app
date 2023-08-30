package rs.raf.rmaprojekat.presentation.view.recycler.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.rmaprojekat.data.models.category.Category
import rs.raf.rmaprojekat.databinding.LayoutItemCategoryBinding
import java.util.function.Consumer

class CategoryViewHolder(
    private val itemBinding: LayoutItemCategoryBinding,
    private val onCategoryClicked: Consumer<Category>,
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(category: Category) {

        itemBinding.titleTv.text = category.strCategory
        Glide.with(itemView)
            .load(category.strCategoryThumb)
            .into(itemBinding.categoryImageView)

        itemBinding.moreBtn.setOnClickListener {
            val description = category.strCategoryDescription
            val categoryName = category.strCategory
            showDescriptionDialog(itemView.context, description, categoryName)
        }

        itemBinding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onCategoryClicked.accept(category)

//                Toast.makeText(category.strCategory);
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