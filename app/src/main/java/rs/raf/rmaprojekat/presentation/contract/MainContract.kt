package rs.raf.rmaprojekat.presentation.contract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.models.category.Category
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.data.models.saved.AreaCount
import rs.raf.rmaprojekat.data.models.saved.CategoryCount
import rs.raf.rmaprojekat.data.models.saved.MealCount
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.data.models.user.User
import rs.raf.rmaprojekat.presentation.view.states.*

interface MainContract {

    interface AreaViewModel {

        val areasState: LiveData<AreasState>
        val addDone: LiveData<AddState>

        fun fetchAll()
        fun getAll()
        fun getByName(name: String)
        fun add(area: Area)
    }

    interface CategoryViewModel {

        val categoriesState: LiveData<CategoriesState>
        val addDone: LiveData<AddState>

        fun fetchAll()
        fun getAll()
        fun getByName(name: String)
        fun add(category: Category)
    }

    interface IngredientViewModel {

        val ingredientsState: LiveData<IngredientsState>
        val addDone: LiveData<AddState>

        fun fetchAll()
        fun getAll()
        fun getByName(name: String)
        fun add(area: Ingredient)
    }

    interface MealViewModel {

        val mealsState: LiveData<MealsState>
        val addDone: LiveData<AddState>

        fun getAll()
        fun getByName(name: String)
        fun getFirstByName(name: String)
        fun fetchByName(name: String)
        fun fetchByCategory(category: String)
        fun fetchByArea(meal: String)
        fun fetchByIngredient(ingredient: String)
        fun add(meal: Meal)
        fun getByNameOrIngredient(name: String)
    }

    interface SavedMealViewModel {

        val mealsState: LiveData<SavedMealsState>
        val top: LiveData<List<CategoryCount>>
        val topAreas: LiveData<List<AreaCount>>
        val topMeals: LiveData<List<MealCount>>
        val addDone: MutableLiveData<AddState>
        val ratio: MutableLiveData<Double>

        fun getAll()
        fun getByName(name: String)
        fun add(meal: SavedMeal)
        fun delete(meal: SavedMeal)
        fun update(meal: SavedMeal)
        fun getTopCategories(number: Int)
        fun getTopAreas(number: Int)
        fun getTopMeals(number: Int)
        fun getRatio(selected: String, name: String)
    }

    interface UserViewModel {

        val mealsState: LiveData<UserState>
        val addDone: LiveData<AddState>

        fun getAll()
        fun getByName(name: String)
        fun add(meal: User): Long
        fun delete(meal: User)
    }
}