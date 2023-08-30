package rs.raf.rmaprojekat.data.models.area

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AreasResponse (
    @Json(name = "meals")
    val all: List<AreaResponse>
)
