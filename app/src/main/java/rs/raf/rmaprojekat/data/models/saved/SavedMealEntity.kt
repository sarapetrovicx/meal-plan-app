package rs.raf.rmaprojekat.data.models.saved

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import rs.raf.rmaprojekat.data.models.user.UserEntity

@Entity(
    tableName = "saved_meals",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)data class SavedMealEntity(
    @PrimaryKey(autoGenerate = true)
    val idMeal: Long,
    val strMeal: String,
    val strArea: String? = "",
    val strCategory: String? = "",
    val strInstructions: String?= "",
    val strYoutube: String?= "",
    var ingredients: List<String?> = listOf(),
    var measures: List<String?> = listOf(),
    val strMealThumb: String?,
    val date: Long,
    val type: String,
    val userId: Long

)