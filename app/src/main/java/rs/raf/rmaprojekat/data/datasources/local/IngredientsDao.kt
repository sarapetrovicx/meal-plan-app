package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.ingredients.IngredientEntity

@Dao
abstract class IngredientsDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: IngredientEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<IngredientEntity>): Completable

    @Query("SELECT * FROM ingredients")
    abstract fun getAll(): Observable<List<IngredientEntity>>

    @Query("DELETE FROM ingredients")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(entities: List<IngredientEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Query("SELECT * FROM ingredients WHERE strIngredient LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<IngredientEntity>>

}