package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.repositories.saved.SavedMealRepository
import rs.raf.rmaprojekat.data.repositories.saved.SavedMealRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.SavedMealViewModel

val savedMealModule = module {

    viewModel { SavedMealViewModel(mealRepository = get()) }

    single<SavedMealRepository> { SavedMealRepositoryImpl(localDataSource = get()) }

    single { get<MainDataBase>().getSavedMealDao() }

}

