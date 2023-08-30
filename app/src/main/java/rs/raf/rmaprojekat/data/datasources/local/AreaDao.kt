package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.area.AreaEntity


@Dao
abstract class AreaDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: AreaEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<AreaEntity>): Completable

    @Query("SELECT * FROM areas")
    abstract fun getAll(): Observable<List<AreaEntity>>

    @Query("DELETE FROM areas")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(entities: List<AreaEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Query("SELECT * FROM areas WHERE strArea LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<AreaEntity>>

}