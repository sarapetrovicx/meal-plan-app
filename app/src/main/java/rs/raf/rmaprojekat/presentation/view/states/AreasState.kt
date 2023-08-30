package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.area.Area

sealed class AreasState {
    object Loading: AreasState()
    object DataFetched: AreasState()
    data class Success(val categories: List<Area>): AreasState()
    data class Error(val message: String): AreasState()
}