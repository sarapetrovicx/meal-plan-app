package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.datasources.remote.MealService
import rs.raf.rmaprojekat.data.repositories.meal.MealRepository
import rs.raf.rmaprojekat.data.repositories.meal.MealRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel

val mealModule = module {

    viewModel { MealViewModel(mealRepository = get()) }

    single<MealRepository> { MealRepositoryImpl(localDataSource = get(), remoteDataSource = get()) }

    single { get<MainDataBase>().getMealDao() }

    single<MealService> { create(retrofit = get()) }

}

