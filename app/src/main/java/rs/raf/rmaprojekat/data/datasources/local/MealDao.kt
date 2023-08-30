package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.meal.MealEntity

@Dao
abstract class MealDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: MealEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<MealEntity>): Completable

    @Query("SELECT * FROM meals")
    abstract fun getAll(): Observable<List<MealEntity>>

    @Query("DELETE FROM meals")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(entities: List<MealEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Update
    abstract fun updateAll(entities: List<MealEntity>): Completable

    @Query("SELECT * FROM meals WHERE strMeal LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE strMeal LIKE :name || '%' LIMIT 1")
    abstract fun getFirstByName(name: String): Observable<MealEntity?>

    @Query("SELECT * FROM meals WHERE strMeal LIKE :name || '%'")
    abstract fun getByIngredient(name: String): Observable<List<MealEntity>>

}