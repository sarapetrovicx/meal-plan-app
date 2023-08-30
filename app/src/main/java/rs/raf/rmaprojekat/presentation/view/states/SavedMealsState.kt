package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.saved.SavedMeal

sealed class SavedMealsState {
    object Loading: SavedMealsState()
    object DataFetched: SavedMealsState()
    data class Success(val saved: List<SavedMeal>): SavedMealsState()
    data class Error(val message: String): SavedMealsState()
}