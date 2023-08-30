package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.datasources.remote.CategoryService
import rs.raf.rmaprojekat.data.repositories.category.CategoryRepository
import rs.raf.rmaprojekat.data.repositories.category.CategoryRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.CategoryViewModel

val categoryModule = module {

    viewModel { CategoryViewModel(categoryRepository = get()) }

    single<CategoryRepository> { CategoryRepositoryImpl(localDataSource = get(), remoteDataSource = get()) }

    single { get<MainDataBase>().getCategoryDao() }

    single<CategoryService> { create(retrofit = get()) }


}

