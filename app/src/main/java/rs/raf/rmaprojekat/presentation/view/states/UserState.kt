package rs.raf.rmaprojekat.presentation.view.states

import rs.raf.rmaprojekat.data.models.user.User

sealed class UserState {
    object Loading: UserState()
    object DataFetched: UserState()
    data class Success(val categories: List<User>): UserState()
    data class Error(val message: String): UserState()
}