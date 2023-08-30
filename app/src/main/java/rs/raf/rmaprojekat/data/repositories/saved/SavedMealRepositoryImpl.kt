package rs.raf.rmaprojekat.data.repositories.saved

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.SavedMealDao
import rs.raf.rmaprojekat.data.models.saved.*

class SavedMealRepositoryImpl(
    private val localDataSource: SavedMealDao,
) : SavedMealRepository {

    override fun getAll(): Observable<List<SavedMeal>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    SavedMeal(
                        it.idMeal,
                        it.strMeal,
                        it.strArea,
                        it.strCategory,
                        it.strInstructions,
                        it.strYoutube,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb,
                        it.date,
                        it.type,
                        it.userId
                    )
                }
            }
    }

    override fun getByName(name: String): Observable<SavedMeal?> {
        return localDataSource
            .getByName(name)
            .map { mealsEntities ->
                if (mealsEntities.isNotEmpty()) {
                    val it = mealsEntities[0]
                    SavedMeal(
                        it.idMeal,
                        it.strMeal,
                        it.strArea,
                        it.strCategory,
                        it.strInstructions,
                        it.strYoutube,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb,
                        it.date,
                        it.type,
                        it.userId
                    )
                } else {
                    null
                }
            }
    }


    override fun getAllByName(name: String): Observable<List<SavedMeal>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    SavedMeal(
                        it.idMeal,
                        it.strMeal,
                        it.strArea,
                        it.strCategory,
                        it.strInstructions,
                        it.strYoutube,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb,
                        it.date,
                        it.type,
                        it.userId
                    )
                }
            }
    }


    override fun insert(it: SavedMeal): Completable {
        val categoryEntity =
            SavedMealEntity(
                it.idMeal,
                it.strMeal,
                it.strArea,
                it.strCategory,
                it.strInstructions,
                it.strYoutube,
                it.ingredients,
                it.measures,
                it.strMealThumb,
                it.date,
                it.type,
                it.userId
            )
        return localDataSource
            .insert(categoryEntity)
    }

    override fun delete(it: SavedMeal): Completable {
        return localDataSource
            .deleteById(it.idMeal.toString())
    }

    override fun update(it: SavedMeal): Completable {
        return localDataSource
            .updateAll(listOf(
                SavedMealEntity(
                    it.idMeal,
                    it.strMeal,
                    it.strArea,
                    it.strCategory,
                    it.strInstructions,
                    it.strYoutube,
                    it.ingredients,
                    it.measures,
                    it.strMealThumb,
                    it.date,
                    it.type,
                    it.userId
                )))
    }

    override fun getTopCategories(number: Int): Observable<List<CategoryCount>> {
        return localDataSource.getTopCategories(number)
    }

    override fun getTopAreas(number: Int): Observable<List<AreaCount>> {
        return localDataSource.getTopAreas(number)
    }

    override fun getTopMeals(number: Int): Observable<List<MealCount>> {
        return localDataSource.getTopMeals(number)
    }

    override fun getRatio(selected: String, name: String): Observable<Double>{
       return localDataSource.getRatio(selected, name)
    }


}