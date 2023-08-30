package rs.raf.rmaprojekat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.rmaprojekat.data.models.saved.AreaCount
import rs.raf.rmaprojekat.data.models.saved.CategoryCount
import rs.raf.rmaprojekat.data.models.saved.MealCount
import rs.raf.rmaprojekat.data.models.saved.SavedMeal
import rs.raf.rmaprojekat.data.repositories.saved.SavedMealRepository
import rs.raf.rmaprojekat.presentation.contract.MainContract
import rs.raf.rmaprojekat.presentation.view.states.AddState
import rs.raf.rmaprojekat.presentation.view.states.SavedMealsState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SavedMealViewModel(
    private val mealRepository: SavedMealRepository,
) : ViewModel(), MainContract.SavedMealViewModel {

    private val subscriptions = CompositeDisposable()
    override val mealsState: MutableLiveData<SavedMealsState> = MutableLiveData()
    override val addDone: MutableLiveData<AddState> = MutableLiveData()
    override val top: MutableLiveData<List<CategoryCount>> = MutableLiveData()
    override val topAreas: MutableLiveData<List<AreaCount>> = MutableLiveData()
    override val topMeals: MutableLiveData<List<MealCount>> = MutableLiveData()
    override val ratio: MutableLiveData<Double> = MutableLiveData()


    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    init {
        val subscription = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                mealRepository.getAllByName(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    mealsState.value = SavedMealsState.Success(it)
                },
                {
                    mealsState.value = SavedMealsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }



    override fun getAll() {
        val subscription = mealRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    mealsState.value = SavedMealsState.Success(it)
                },
                {
                    mealsState.value = SavedMealsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getByName(name: String) {
        publishSubject.onNext(name)
    }


    override fun add(meal: SavedMeal) {
        val subscription = mealRepository
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

    override fun delete(meal: SavedMeal){
        val subscription = mealRepository
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

    override fun update(meal: SavedMeal){
        val subscription = mealRepository
            .update(meal)
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

    override fun getTopCategories(number: Int){
        val subscription =  mealRepository
            .getTopCategories(number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    top.value = it
                },
                {
                    addDone.value = AddState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }
    override fun getTopAreas(number: Int){
        val subscription =  mealRepository
            .getTopAreas(number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    topAreas.value = it
                },
                {
                    addDone.value = AddState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }
    override fun getTopMeals(number: Int){
        val subscription =  mealRepository
            .getTopMeals(number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    topMeals.value = it
                },
                {
                    addDone.value = AddState.Error("Error happened while adding movie")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }
    override fun getRatio(selected: String, name: String){
        val subscription =  mealRepository
            .getRatio(selected, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    ratio.value = it
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