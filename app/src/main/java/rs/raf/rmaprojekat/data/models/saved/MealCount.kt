package rs.raf.rmaprojekat.data.models.saved

data class MealCount(
    val strMeal: String,
    val ratio: Double
) : DisplayableItem {
    override fun getDisplayText(): String {
        return strMeal
    }
    override fun getDisplayCount(): String {
        return ratio.toString()
    }
}
