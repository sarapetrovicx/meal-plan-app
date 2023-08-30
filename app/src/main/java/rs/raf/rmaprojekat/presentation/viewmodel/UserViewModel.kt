package rs.raf.rmaprojekat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.rmaprojekat.data.models.user.User
import rs.raf.rmaprojekat.data.repositories.user.UserRepository
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.states.AddState
import rs.raf.rmaprojekat.presentation.view.states.UserState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class UserViewModel(
    private val userRepository: UserRepository,
) : ViewModel(), MainContract.UserViewModel {

    private val subscriptions = CompositeDisposable()
    override val mealsState: MutableLiveData<UserState> = MutableLiveData()
    override val addDone: MutableLiveData<AddState> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
        val subscription = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                userRepository.getAllByName(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    mealsState.value = UserState.Success(it)
                },
                {
                    mealsState.value = UserState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }


    override fun getAll() {
        val subscription = userRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealsState.value = UserState.Success(it)
                },
                {
                    mealsState.value = UserState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getByName(name: String) {
        publishSubject.onNext(name)
    }


    override fun add(meal: User) :Long {
        var id: Long = 1;
        val subscription = userRepository
            .insert(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    id = meal.id
                    addDone.value = AddState.Success
                },
                {
                    addDone.value = AddState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
        return id
    }

    override fun delete(meal: User){
        val subscription = userRepository
            .delete(meal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    addDone.value = AddState.Success
                },
                {
                    addDone.value = AddState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }


    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}