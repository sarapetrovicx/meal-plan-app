package rs.raf.rmaprojekat.data.repositories.ingredients

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient


interface IngredientRepository {
    fun fetchAll(): Observable<Resource<Unit>>
    fun getAll(): Observable<List<Ingredient>>
    fun getAllByName(name: String): Observable<List<Ingredient>>
    fun insert(meal: Ingredient): Completable

}