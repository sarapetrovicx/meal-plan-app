package rs.raf.rmaprojekat.data.repositories.ingredients

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.IngredientsDao
import rs.raf.rmaprojekat.data.datasources.remote.IngredientService
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.data.models.ingredients.IngredientEntity


import timber.log.Timber

class IngredientRepositoryImpl(
    private val localDataSource: IngredientsDao,
    private val remoteDataSource: IngredientService
) : IngredientRepository {

    override fun fetchAll(): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAll()
            .doOnNext {
                Timber.e("Upis u bazu")
                val entities = it.all.map {
                    IngredientEntity(
                        it.idIngredient,
                        it.strIngredient,
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }

    }

    override fun getAll(): Observable<List<Ingredient>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    Ingredient(it.idIngredient, it.strIngredient)
                }
            }
    }

    override fun getAllByName(name: String): Observable<List<Ingredient>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    Ingredient(it.idIngredient, it.strIngredient)
                }
            }
    }

    override fun insert(meal: Ingredient): Completable {
        val categoryEntity = IngredientEntity(meal.idIngredient, meal.strIngredient)
        return localDataSource
            .insert(categoryEntity)
    }

}