package rs.raf.rmaprojekat.data.models.saved

data class AreaCount(
    val strArea: String,
    val ratio: Double
) : DisplayableItem {
    override fun getDisplayText(): String {
        return strArea
    }

    override fun getDisplayCount(): String {
        return ratio.toString()
    }
}

