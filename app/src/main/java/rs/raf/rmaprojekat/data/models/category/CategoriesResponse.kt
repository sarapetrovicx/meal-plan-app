package rs.raf.rmaprojekat.data.models.category

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesResponse (
    @Json(name = "categories")
    val allCategories: List<CategoryResponse>
)
