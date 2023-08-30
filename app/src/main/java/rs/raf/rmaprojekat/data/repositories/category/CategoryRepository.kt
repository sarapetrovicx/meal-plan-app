package rs.raf.rmaprojekat.data.repositories.category

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.category.Category

interface CategoryRepository {
    fun fetchAll(): Observable<Resource<Unit>>
    fun getAll(): Observable<List<Category>>
    fun getAllByName(name: String): Observable<List<Category>>
    fun insert(movie: Category): Completable

}