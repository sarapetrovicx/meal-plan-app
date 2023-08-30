package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.ingredients.Ingredient

sealed class IngredientsState {
    object Loading: IngredientsState()
    object DataFetched: IngredientsState()
    data class Success(val categories: List<Ingredient>): IngredientsState()
    data class Error(val message: String): IngredientsState()
}