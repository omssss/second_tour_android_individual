package ru.nto.guestmanagement.mobile.task.a3


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState

/** A3. Логика загрузки и обновления данных пропуска. */
class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope,
) {
    private var loadJob: Job? = null
    val state: StateFlow<PassScreenState> = MutableStateFlow(PassScreenState.InitialLoading)

    fun start() {
        if (loadJob?.isActive == true) return
        if (state.value !is PassScreenState.InitialLoading) return
        loadJob = scope.launch {
            repository.load()
                .onFailure { error ->
                    (state as MutableStateFlow<PassScreenState>).update {
                        PassScreenState.InitialError(
                            message = error.message ?: "Не удалось загрузить пропуск"
                        )
                    }
                }
                .onSuccess { data ->
                    (state as MutableStateFlow<PassScreenState>).update { current ->
                        if (current is PassScreenState.Content) {
                            current.copy(data = data, isRefreshing = false, isOffline = false)
                        } else {
                            PassScreenState.Content(data, isRefreshing = false, isOffline = false)
                        }
                    }
                }
        }
    }

    fun refresh() {
        if (loadJob?.isActive == true) return
        if (state.value !is PassScreenState.Content) return
        (state as MutableStateFlow<PassScreenState>).update { current ->
            if (current is PassScreenState.Content) {
                current.copy(isRefreshing = true, isOffline = false)
            } else {
                current
            }
        }
        loadJob = scope.launch {
            repository.load()
                .onFailure {
                    state.update { current ->
                        if (current is PassScreenState.Content) {
                            current.copy(isRefreshing = false, isOffline = true)
                        } else {
                            current
                        }
                    }
                }
                .onSuccess { data ->
                    state.update { current ->
                        if (current is PassScreenState.Content) {
                            current.copy(data = data, isRefreshing = false, isOffline = false)
                        } else {
                            PassScreenState.Content(data, isRefreshing = false, isOffline = false)
                        }
                    }
                }
        }
    }

    fun retry() {
        if (loadJob?.isActive == true) return
        if (state.value !is PassScreenState.InitialError) return
        (state as MutableStateFlow<PassScreenState>).update {
            PassScreenState.InitialLoading
        }
        start()
    }
}
