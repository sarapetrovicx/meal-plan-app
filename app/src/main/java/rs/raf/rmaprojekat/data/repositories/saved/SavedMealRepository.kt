package rs.raf.rmaprojekat.data.repositories.saved

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.saved.AreaCount
import rs.raf.rmaprojekat.data.models.saved.CategoryCount
import rs.raf.rmaprojekat.data.models.saved.MealCount
import rs.raf.rmaprojekat.data.models.saved.SavedMeal


interface SavedMealRepository {

    fun getAll(): Observable<List<SavedMeal>>
    fun getAllByName(name: String): Observable<List<SavedMeal>>
    fun getByName(name: String): Observable<SavedMeal?>
    fun insert(meal: SavedMeal): Completable
    fun delete(meal: SavedMeal): Completable
    fun update(meal: SavedMeal): Completable
    fun getTopCategories(number: Int): Observable<List<CategoryCount>>
    fun getTopAreas(number: Int): Observable<List<AreaCount>>
    fun getTopMeals(number: Int): Observable<List<MealCount>>
    fun getRatio(selected: String, name: String): Observable<Double>
}