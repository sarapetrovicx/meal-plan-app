package rs.raf.rmaprojekat.data.models.meal

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealsResponse (
    @Json(name = "meals")
    val all: List<MealResponse>
)
