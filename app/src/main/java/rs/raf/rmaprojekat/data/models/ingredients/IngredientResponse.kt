package rs.raf.rmaprojekat.data.models.ingredients

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientResponse(
    val idIngredient: String,
    val strIngredient: String,
)