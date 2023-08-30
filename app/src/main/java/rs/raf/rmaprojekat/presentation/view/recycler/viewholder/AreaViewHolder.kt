package rs.raf.rmaprojekat.presentation.view.recycler.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.models.category.Category
import rs.raf.rmaprojekat.databinding.LayoutItemAreaBinding
import rs.raf.rmaprojekat.databinding.LayoutItemCategoryBinding
import java.util.function.Consumer

class AreaViewHolder(
    private val itemBinding: LayoutItemAreaBinding,
    private val onItemClicked: Consumer<Area>,
) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(area: Area) {

        itemBinding.titleTv.text = area.strArea

        itemBinding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked.accept(area)
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