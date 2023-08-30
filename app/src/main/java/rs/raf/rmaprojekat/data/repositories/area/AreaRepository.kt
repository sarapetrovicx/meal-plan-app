package rs.raf.rmaprojekat.data.repositories.area

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.area.Area


interface AreaRepository {
    fun fetchAll(): Observable<Resource<Unit>>
    fun getAll(): Observable<List<Area>>
    fun getAllByName(name: String): Observable<List<Area>>
    fun insert(area: Area): Completable

}