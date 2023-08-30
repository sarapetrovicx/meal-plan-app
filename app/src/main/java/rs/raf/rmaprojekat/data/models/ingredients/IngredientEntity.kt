package rs.raf.rmaprojekat.data.models.ingredients

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity (
    @PrimaryKey
    val idIngredient: String,
    val strIngredient: String,
)