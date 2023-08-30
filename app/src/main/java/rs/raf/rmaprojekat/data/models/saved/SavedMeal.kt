package rs.raf.rmaprojekat.data.models.saved

data class SavedMeal(
    val idMeal: Long,
    val strMeal: String,
    val strArea: String?,
    val strCategory: String?,
    val strInstructions: String?,
    val strYoutube: String?,
    var ingredients: List<String?> = listOf(),
    var measures: List<String?> = listOf(),
    val strMealThumb: String?,
    val date: Long,
    val type: String,
    val userId: Long
)