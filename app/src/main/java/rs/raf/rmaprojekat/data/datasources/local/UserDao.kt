package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.user.UserEntity

@Dao
abstract class UserDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: UserEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<UserEntity>): Completable

    @Query("SELECT * FROM users")
    abstract fun getAll(): Observable<List<UserEntity>>

    @Query("DELETE FROM users")
    abstract fun deleteAll()

    @Query("DELETE FROM users WHERE id = :id")
    abstract fun deleteById(id: Long): Completable

    @Transaction
    open fun deleteAndInsertAll(entities: List<UserEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Query("SELECT * FROM users WHERE username LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<UserEntity>>


}