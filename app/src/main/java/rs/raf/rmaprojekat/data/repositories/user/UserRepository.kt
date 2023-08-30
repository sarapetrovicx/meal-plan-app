package rs.raf.rmaprojekat.data.repositories.user

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.user.User


interface UserRepository {

    fun getAll(): Observable<List<User>>
    fun getAllByName(name: String): Observable<List<User>>
    fun getByName(name: String): Observable<User?>
    fun insert(meal: User): Completable
    fun delete(meal: User): Completable

}