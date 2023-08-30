package rs.raf.rmaprojekat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.rmaprojekat.data.models.Resource
import rs.raf.rmaprojekat.data.models.ingredients.Ingredient
import rs.raf.rmaprojekat.data.repositories.ingredients.IngredientRepository
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.states.AddState
import rs.raf.rmaprojekat.presentation.view.states.IngredientsState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class IngredientViewModel(
    private val ingredientRepository: IngredientRepository,
) : ViewModel(), MainContract.IngredientViewModel {

    private val subscriptions = CompositeDisposable()
    override val ingredientsState: MutableLiveData<IngredientsState> = MutableLiveData()
    override val addDone: MutableLiveData<AddState> = MutableLiveData()

    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
        val subscription = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                ingredientRepository.getAllByName(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    ingredientsState.value = IngredientsState.Success(it)
                },
                {
                    ingredientsState.value = IngredientsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun fetchAll() {
        val subscription = ingredientRepository
            .fetchAll()
            .startWith(Resource.Loading()) //Kada se pokrene fetch hocemo da postavimo stanje na Loading
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    when(it) {
                        is Resource.Loading -> ingredientsState.value = IngredientsState.Loading
                        is Resource.Success -> ingredientsState.value = IngredientsState.DataFetched
                        is Resource.Error -> ingredientsState.value = IngredientsState.Error("Error happened while fetching data from the server")
                    }
                },
                {
                    ingredientsState.value = IngredientsState.Error("Error happened while fetching data from the server")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAll() {
        val subscription = ingredientRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    ingredientsState.value = IngredientsState.Success(it)
                },
                {
                    ingredientsState.value = IngredientsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getByName(name: String) {
        publishSubject.onNext(name)
    }

    override fun add(meal: Ingredient) {
        val subscription = ingredientRepository
            .insert(meal)
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