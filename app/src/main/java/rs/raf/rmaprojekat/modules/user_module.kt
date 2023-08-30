package rs.raf.rmaprojekat.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.rmaprojekat.data.datasources.local.MainDataBase
import rs.raf.rmaprojekat.data.repositories.user.UserRepository
import rs.raf.rmaprojekat.data.repositories.user.UserRepositoryImpl
import rs.raf.rmaprojekat.presentation.viewmodel.UserViewModel

val userModule = module {

    viewModel { UserViewModel(userRepository = get()) }

    single<UserRepository> { UserRepositoryImpl(localDataSource = get()) }

    single { get<MainDataBase>().getUserDao() }

}

