package rs.raf.rmaprojekat.presentation.view.states

sealed class AddState {
    object Success: AddState()
    data class Error(val message: String): AddState()
}