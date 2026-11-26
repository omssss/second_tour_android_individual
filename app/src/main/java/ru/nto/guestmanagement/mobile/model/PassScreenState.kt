package ru.nto.guestmanagement.mobile.model


sealed interface PassScreenState {
    data object InitialLoading : PassScreenState
    data class InitialError(val message: String) : PassScreenState
    data class Content(
        val data: PassData,
        val isRefreshing: Boolean,
        val isOffline: Boolean,
    ) : PassScreenState
}
