package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.datasources.remote.AreaService
import rs.raf.rmaprojekat.data.datasources.remote.MealService
import rs.raf.rmaprojekat.data.repositories.area.AreaRepository
import rs.raf.rmaprojekat.data.repositories.area.AreaRepositoryImpl
import rs.raf.rmaprojekat.data.repositories.meal.MealRepository
import rs.raf.rmaprojekat.data.repositories.meal.MealRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.AreaViewModel
import rs.raf.rmaprojekat.presentation.viewmodel.MealViewModel

val areaModule = module {

    viewModel { AreaViewModel(areaRepository = get()) }

    single<AreaRepository> { AreaRepositoryImpl(localDataSource = get(), remoteDataSource = get()) }

    single { get<MainDataBase>().getAreaDao() }

    single<AreaService> { create(retrofit = get()) }

}

