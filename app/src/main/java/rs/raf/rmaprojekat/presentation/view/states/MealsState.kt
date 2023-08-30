package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.meal.Meal

sealed class MealsState {
    object Loading: MealsState()
    object DataFetched: MealsState()
    data class Success(val meals: List<Meal>): MealsState()
    data class Error(val message: String): MealsState()
}