package rs.raf.rmaprojekat.data.models.meal

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strYoutube: String?,
    var tags: String?,
    var ingredients: List<String?> = listOf(),
    var measures: List<String?> = listOf(),
    val strMealThumb: String?,
)