package rs.raf.rmaprojekat.presentation.view.recycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rs.raf.rmaprojekat.data.models.user.User
import rs.raf.rmaprojekat.databinding.LayoutItemSavedMealBinding
import java.util.function.Consumer


class UserViewHolder(
    private val itemBinding: LayoutItemSavedMealBinding,
    private val onItemClicked: Consumer<User>,
    private val onEditClicked: Consumer<User>,
    private val onDeleteClicked: Consumer<User>,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(meal: User) {
//        itemBinding.titleTv.text = meal.strMeal

//        Glide.with(itemView)
//            .load(meal.strMealThumb)
//            .into(itemBinding.mealImageView)

        itemBinding.editBtn.setOnClickListener {
            onEditClicked.accept(meal)
        }

        itemBinding.deleteBtn.setOnClickListener {
            onDeleteClicked.accept(meal)
        }

        itemBinding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked.accept(meal)
            }

        }
    }

}