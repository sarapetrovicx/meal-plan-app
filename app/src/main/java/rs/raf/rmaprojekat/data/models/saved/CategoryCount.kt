package rs.raf.rmaprojekat.data.models.saved

data class CategoryCount(
    val strCategory: String,
    val categoryRatio: Double
) : DisplayableItem {
    override fun getDisplayText(): String {
        return strCategory
    }
    override fun getDisplayCount(): String {
        return categoryRatio.toString()
    }
}

