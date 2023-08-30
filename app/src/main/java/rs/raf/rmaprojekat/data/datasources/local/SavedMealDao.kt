package rs.raf.rmaprojekat.data.datasources.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.models.saved.AreaCount
import rs.raf.rmaprojekat.data.models.saved.CategoryCount
import rs.raf.rmaprojekat.data.models.saved.MealCount
import rs.raf.rmaprojekat.data.models.saved.SavedMealEntity

@Dao
abstract class SavedMealDao {

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insert(entity: SavedMealEntity): Completable

    @Insert( onConflict = OnConflictStrategy.REPLACE )
    abstract fun insertAll(entities: List<SavedMealEntity>): Completable

    @Query("SELECT * FROM saved_meals")
    abstract fun getAll(): Observable<List<SavedMealEntity>>

    @Query("DELETE FROM saved_meals")
    abstract fun deleteAll()

    @Query("DELETE FROM saved_meals WHERE idMeal = :id")
    abstract fun deleteById(id: String): Completable

    @Transaction
    open fun deleteAndInsertAll(entities: List<SavedMealEntity>) {
        deleteAll()
        insertAll(entities).blockingAwait()
    }

    @Update
    abstract fun updateAll(entities: List<SavedMealEntity>): Completable

    @Query("SELECT * FROM saved_meals WHERE strMeal LIKE :name || '%'")
    abstract fun getByName(name: String): Observable<List<SavedMealEntity>>

    @Query("SELECT strCategory, (COUNT(*) * 1.0 / (SELECT COUNT(*) FROM saved_meals)) as categoryRatio FROM saved_meals GROUP BY strCategory ORDER BY categoryRatio DESC LIMIT :number")
    abstract fun getTopCategories(number: Int): Observable<List<CategoryCount>>

    @Query("SELECT strMeal, (COUNT(*) * 1.0 / (SELECT COUNT(*) FROM saved_meals)) as ratio FROM saved_meals GROUP BY strMeal ORDER BY ratio DESC LIMIT :number")
    abstract fun getTopMeals(number: Int): Observable<List<MealCount>>

    @Query("SELECT strArea, (COUNT(*) * 1.0 / (SELECT COUNT(*) FROM saved_meals)) as ratio FROM saved_meals GROUP BY strArea ORDER BY ratio DESC LIMIT :number")
    abstract fun getTopAreas(number: Int): Observable<List<AreaCount>>

    @Query("SELECT (SELECT COUNT(*) FROM saved_meals WHERE :selected = :name) * 1.0 / COUNT(*) as categoryRatio FROM saved_meals")
    abstract fun getRatio(selected: String, name: String): Observable<Double>



}