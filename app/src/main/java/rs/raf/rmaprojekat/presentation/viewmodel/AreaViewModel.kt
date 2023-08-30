package rs.raf.rmaprojekat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.area.Area
import rs.raf.rmaprojekat.data.repositories.area.AreaRepository
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.states.AddState
import rs.raf.rmaprojekat.presentation.view.states.AreasState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AreaViewModel(
    private val areaRepository: AreaRepository,
) : ViewModel(), MainContract.AreaViewModel {

    private val subscriptions = CompositeDisposable()
    override val areasState: MutableLiveData<AreasState> = MutableLiveData()
    override val addDone: MutableLiveData<AddState> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
        val subscription = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                areaRepository.getAllByName(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    areasState.value = AreasState.Success(it)
                },
                {
                    areasState.value = AreasState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAll() {
        val subscription = areaRepository
            .fetchAll()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> areasState.value = AreasState.Loading
                        is Resource.Success -> areasState.value = AreasState.DataFetched
                        is Resource.Error -> areasState.value = AreasState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    areasState.value = AreasState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAll() {
        val subscription = areaRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    areasState.value = AreasState.Success(it)
                },
                {
                    areasState.value = AreasState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getByName(name: String) {
        publishSubject.onNext(name)
    }

    override fun add(area: Area) {
        val subscription = areaRepository
            .insert(area)
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