package rs.raf.rmaprojekat.data.models.ingredients

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IngredientsResponse (
    @Json(name = "meals")
    val all: List<IngredientResponse>
)
