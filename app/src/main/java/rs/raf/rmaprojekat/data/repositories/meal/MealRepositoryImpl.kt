package rs.raf.rmaprojekat.data.repositories.meal

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.MealDao
import rs.raf.rmaprojekat.data.datasources.remote.MealService
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.meal.Meal
import rs.raf.rmaprojekat.data.models.meal.MealEntity


import timber.log.Timber

class MealRepositoryImpl(
    private val localDataSource: MealDao,
    private val remoteDataSource: MealService
) : MealRepository {

    override fun fetchByName(name: String): Observable<Resource<Unit>>  {
        return remoteDataSource
            .getByName(name)
            .doOnNext { it ->
                Timber.e("Upis u bazu")
                val entities = it.all.map { it1 ->
                    MealEntity(
                        it1.idMeal,
                        it1.strMeal,
                        it1.strCategory,
                        it1.strArea,
                        it1.strInstructions,
                        it1.strYoutube,
                        it1.strTags,
                        listOf(it1.strIngredient1, it1.strIngredient2, it1.strIngredient3, it1.strIngredient4,
                            it1.strIngredient5, it1.strIngredient6, it1.strIngredient7, it1.strIngredient8,
                            it1.strIngredient9,it1.strIngredient10, it1.strIngredient11, it1.strIngredient12,
                            it1.strIngredient13,it1.strIngredient14, it1.strIngredient15, it1.strIngredient16,
                            it1.strIngredient17, it1.strIngredient18, it1.strIngredient19, it1.strIngredient20)
                            .filter { it!!.isNotEmpty() },
                        listOf(
                            it1.strMeasure1, it1.strMeasure2, it1.strMeasure3, it1.strMeasure4, it1.strMeasure5,
                            it1.strMeasure6, it1.strMeasure7, it1.strMeasure8, it1.strMeasure9, it1.strMeasure10,
                            it1.strMeasure11, it1.strMeasure12, it1.strMeasure13, it1.strMeasure14, it1.strMeasure15,
                            it1.strMeasure16, it1.strMeasure17, it1.strMeasure18, it1.strMeasure19, it1.strMeasure20
                            ).filter { it!!.isNotEmpty() },

                    it1.strMealThumb
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }.map {
                Resource.Success(Unit)
            }
    }

//    override fun fetchByCategory(category: String): Observable<Resource<Unit>> {
//        return remoteDataSource
//            .getByCategory(category)
//            .flatMap { mealsResponse ->
//                Timber.e("Upis u bazu")
//                Observable.fromIterable(mealsResponse.all)
//                    .flatMap { mealResponse ->
//                        remoteDataSource.getByName(mealResponse.strMeal)
//                            .map { response ->
//                                response.all.map { mealResponse1 ->
//                                    MealEntity(
//                                        mealResponse1.idMeal,
//                                        mealResponse1.strMeal,
//                                        mealResponse1.strCategory,
//                                        mealResponse1.strArea,
//                                        mealResponse1.strInstructions,
//                                        mealResponse1.strYoutube,
//                                        mealResponse1.strTags,
//                                        listOf(mealResponse1.strIngredient1, mealResponse1.strIngredient2, mealResponse1.strIngredient3, mealResponse1.strIngredient4,
//                                            mealResponse1.strIngredient5, mealResponse1.strIngredient6, mealResponse1.strIngredient7, mealResponse1.strIngredient8,
//                                            mealResponse1.strIngredient9,mealResponse1.strIngredient10, mealResponse1.strIngredient11, mealResponse1.strIngredient12,
//                                            mealResponse1.strIngredient13,mealResponse1.strIngredient14, mealResponse1.strIngredient15, mealResponse1.strIngredient16,
//                                            mealResponse1.strIngredient17, mealResponse1.strIngredient18, mealResponse1.strIngredient19, mealResponse1.strIngredient20)
//                                            .filter { it!!.isNotEmpty() },
//                                        listOf(
//                                            mealResponse1.strMeasure1, mealResponse1.strMeasure2, mealResponse1.strMeasure3, mealResponse1.strMeasure4, mealResponse1.strMeasure5,
//                                            mealResponse1.strMeasure6, mealResponse1.strMeasure7, mealResponse1.strMeasure8, mealResponse1.strMeasure9, mealResponse1.strMeasure10,
//                                            mealResponse1.strMeasure11, mealResponse1.strMeasure12, mealResponse1.strMeasure13, mealResponse1.strMeasure14, mealResponse1.strMeasure15,
//                                            mealResponse1.strMeasure16, mealResponse1.strMeasure17, mealResponse1.strMeasure18, mealResponse1.strMeasure19, mealResponse1.strMeasure20
//                                        ).filter { it!!.isNotEmpty() },
//                                        mealResponse1.strMealThumb
//                                    )
//                                }
//                            }
//                    }
//            }
//            .toList()
//            .flatMapObservable { entitiesList ->
//                val entities = entitiesList.flatten() // Flatten the list of lists
//                localDataSource.deleteAndInsertAll(entities) // Pass the flattened list
//                Observable.just(Resource.Success(Unit))
//            }
//    }


    override fun fetchByCategory(category: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getByCategory(category)
            .doOnNext {
//                Timber.e("Upis u bazu")
                val entities = it.all.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        "",
                        "",
                        "",
                        "",
                        "",
                        emptyList(),
                        emptyList(),
                        it.strMealThumb
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchByArea(area: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getByArea(area)
            .doOnNext {
                Timber.e("Upis u bazu")
                val entities = it.all.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        "",
                        "",
                        "",
                        "",
                        "",
                        emptyList(),
                        emptyList(),
                        it.strMealThumb
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun fetchByIngredient(ingr: String): Observable<Resource<Unit>> {
        return remoteDataSource
            .getByIngredient(ingr)
            .doOnNext {
                Timber.e("Upis u bazu")
                val entities = it.all.map {
                    MealEntity(
                        it.idMeal,
                        it.strMeal,
                        "",
                        "",
                        "",
                        "",
                        "",
                        emptyList(),
                        emptyList(),
                        it.strMealThumb
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }
    }

    override fun getAll(): Observable<List<Meal>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strYoutube,
                        it.tags,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb
                    )
                }
            }
    }


    override fun getAllByName(name: String): Observable<List<Meal>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strYoutube,
                        it.tags,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb
                    )
                }
            }
    }

    override fun getFirstByName(name: String): Observable<Meal> {
        return localDataSource
            .getFirstByName(name)
            .map {
                Meal(
                    it.idMeal,
                    it.strMeal,
                    it.strCategory,
                    it.strArea,
                    it.strInstructions,
                    it.strYoutube,
                    it.tags,
                    it.ingredients,
                    it.measures,
                    it.strMealThumb
                )
            }
    }
    override fun getAllByIngredient(name: String): Observable<List<Meal>> {
        return localDataSource
            .getByIngredient(name)
            .map {
                it.map {
                    Meal(
                        it.idMeal,
                        it.strMeal,
                        it.strCategory,
                        it.strArea,
                        it.strInstructions,
                        it.strYoutube,
                        it.tags,
                        it.ingredients,
                        it.measures,
                        it.strMealThumb
                    )
                }
            }
    }

    override fun insert(it: Meal): Completable {
        val categoryEntity = MealEntity(
            it.idMeal,
            it.strMeal,
            it.strCategory,
            it.strArea,
            it.strInstructions,
            it.strYoutube,
            it.tags,
            it.ingredients,
            it.measures,
            it.strMealThumb
        )
        return localDataSource
            .insert(categoryEntity)
    }

}