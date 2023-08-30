package rs.raf.rmaprojekat.data.repositories.user

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.rmaprojekat.data.datasources.local.UserDao
import rs.raf.rmaprojekat.data.models.user.User
import rs.raf.rmaprojekat.data.models.user.UserEntity


class UserRepositoryImpl(
    private val localDataSource: UserDao,
) : UserRepository {

    override fun getAll(): Observable<List<User>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    User(
                        it.id,
                        it.username,
                        it.password,
                    )
                }
            }
    }

    override fun getByName(name: String): Observable<User?> {
        return localDataSource
            .getByName(name)
            .map { mealsEntities ->
                if (mealsEntities.isNotEmpty()) {
                    val it = mealsEntities[0]
                    User(
                        it.id,
                        it.username,
                        it.password,
                    )
                } else {
                    null
                }
            }
    }


    override fun getAllByName(name: String): Observable<List<User>> {
        return localDataSource
            .getByName(name)
            .map {
                it.map {
                    User(
                        it.id,
                        it.username,
                        it.password,
                    )
                }
            }
    }


    override fun insert(it: User): Completable {
        val categoryEntity =
            UserEntity(
                it.id,
                it.username,
                it.password,
            )
        return localDataSource
            .insert(categoryEntity)
    }

    override fun delete(it: User): Completable {
        return localDataSource
            .deleteById(it.id)
    }

}