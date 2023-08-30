package rs.raf.rmaprojekat.data.repositories.category

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.CategoryDao
import rs.raf.rmaprojekat.data.datasources.remote.CategoryService
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.category.CategoryEntity
import rs.raf.rmaprojekat.data.models.category.Category

import timber.log.Timber

class CategoryRepositoryImpl(
    private val localDataSource: CategoryDao,
    private val remoteDataSource: CategoryService
) : CategoryRepository {

    override fun fetchAll(): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAll()
            .doOnNext {
                Timber.e("Upis u bazu")
                val entities = it.allCategories.map {
                    CategoryEntity(
                        it.idCategory,
                        it.strCategory,
                        it.strCategoryDescription,
                        it.strCategoryThumb
                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }
        // Kada zelimo sami da kontrolisemo sta se desava sa greskom, umesto da je samo prepustimo
        // error handleru, mozemo iskoristiti operator onErrorReturn, tada sami na osnovu greske
        // odlucujemo koju vrednost cemo da vratimo. Ta vrednost mora biti u skladu sa povratnom
        // vrednoscu lanca.
        // Kada se iskoristi onErrorReturn onError lambda u viewmodelu nece biti izvrsena,
        // nego ce umesdto nje biti izvsena onNext koja ce kao parametar primiti vrednost koja
        // je vracena iz onErrorReturn
        // Obicno se koristi kada je potrebno proveriti koji kod greske je vratio server.
//            .onErrorReturn {
//                when(it) {
//                    is HttpException -> {
//                        when(it.code()) {
//                            in 400..499 -> {
//
//                            }
//                        }
//                    }
//                }
//                Timber.e("ON ERROR RETURN")
//            }
    }

    override fun getAll(): Observable<List<Category>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    Category(it.idCategory, it.strCategory,it.strCategoryDescription, it.strCategoryThumb)
                }
            }
    }

    override fun getAllByName(name: String): Observable<List<Category>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    Category(it.idCategory, it.strCategory, it.strCategoryDescription, it.strCategoryThumb)
                }
            }
    }

    override fun insert(category: Category): Completable {
        val categoryEntity = CategoryEntity(category.idCategory, category.strCategory,  category.strCategoryDescription, category.strCategoryThumb)
        return localDataSource
            .insert(categoryEntity)
    }

}