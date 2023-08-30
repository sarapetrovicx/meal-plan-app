package rs.raf.rmaprojekat.data.repositories.meal

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.meal.Meal


interface MealRepository {

    fun fetchByName(name: String): Observable<Resource<Unit>>
    fun fetchByCategory(category: String): Observable<Resource<Unit>>
    fun fetchByArea(area: String): Observable<Resource<Unit>>
    fun fetchByIngredient(ingr: String): Observable<Resource<Unit>>
    fun getAll(): Observable<List<Meal>>
    fun getAllByName(name: String): Observable<List<Meal>>
    fun getFirstByName(name: String): Observable<Meal>
    fun getAllByIngredient(name: String): Observable<List<Meal>>
    fun insert(meal: Meal): Completable

}