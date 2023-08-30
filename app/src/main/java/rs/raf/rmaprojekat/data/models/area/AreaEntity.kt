package rs.raf.rmaprojekat.data.models.area

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class AreaEntity (
    @PrimaryKey(autoGenerate = true)
    val idArea: Long = 0,
    val strArea: String,
)