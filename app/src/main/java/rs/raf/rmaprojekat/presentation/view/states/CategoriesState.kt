package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.category.Category

sealed class CategoriesState {
    object Loading: CategoriesState()
    object DataFetched: CategoriesState()
    data class Success(val categories: List<Category>): CategoriesState()
    data class Error(val message: String): CategoriesState()
}