package rs.raf.rmaprojekat.data.repositories.area

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.AreaDao
import rs.raf.rmaprojekat.data.datasources.remote.AreaService
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.models.area.AreaEntity


import timber.log.Timber

class AreaRepositoryImpl(
    private val localDataSource: AreaDao,
    private val remoteDataSource: AreaService
) : AreaRepository {

    override fun fetchAll(): Observable<Resource<Unit>> {
        return remoteDataSource
            .getAll()
            .doOnNext {
                Timber.e("Upis u bazu")
                val entities = it.all.map {
                    AreaEntity(strArea = it.strArea)

//                    AreaEntity(
//                        it.strArea,
//                    )
                }
                localDataSource.deleteAndInsertAll(entities)
            }
            .map {
                Resource.Success(Unit)
            }

    }

    override fun getAll(): Observable<List<Area>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    Area(it.idArea, it.strArea)
                }
            }
    }

    override fun getAllByName(name: String): Observable<List<Area>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    Area(it.idArea, it.strArea)
                }
            }
    }

    override fun insert(area: Area): Completable {
        val categoryEntity = AreaEntity(strArea =  area.strArea)
        return localDataSource
            .insert(categoryEntity)
    }

}