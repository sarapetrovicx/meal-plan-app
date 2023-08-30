package rs.raf.rmaprojekat.data.models.area

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AreaResponse(
    val strArea: String,
)