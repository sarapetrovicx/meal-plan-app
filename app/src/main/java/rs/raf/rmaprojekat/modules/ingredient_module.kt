package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.datasources.remote.AreaService
import rs.raf.rmaprojekat.data.datasources.remote.IngredientService
import rs.raf.rmaprojekat.data.datasources.remote.MealService
import rs.raf.rmaprojekat.data.repositories.area.AreaRepository
import rs.raf.rmaprojekat.data.repositories.area.AreaRepositoryImpl
import rs.raf.rmaprojekat.data.repositories.ingredients.IngredientRepository
import rs.raf.rmaprojekat.data.repositories.ingredients.IngredientRepositoryImpl
import rs.raf.rmaprojekat.data.repositories.meal.MealRepository
import rs.raf.rmaprojekat.data.repositories.meal.MealRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.AreaViewModel
import rs.raf.rmaprojekat.presentation.viewmodel.IngredientViewModel
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel

val ingredientModule = module {

    viewModel { IngredientViewModel(ingredientRepository = get()) }

    single<IngredientRepository> { IngredientRepositoryImpl(localDataSource = get(), remoteDataSource = get()) }

    single { get<MainDataBase>().getIngredientsDao() }

    single<IngredientService> { create(retrofit = get()) }

}

