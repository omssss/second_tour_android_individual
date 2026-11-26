package ru.nto.guestmanagement.mobile.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState
import ru.nto.guestmanagement.mobile.task.a3.PassLoadController

class PassViewModel(repository: PassRepository) : ViewModel() {
    private val controller = PassLoadController(repository, viewModelScope)

    val state: StateFlow<PassScreenState> get() = controller.state

    init {
        controller.start()
    }

    fun refresh() = controller.refresh()

    fun retry() = controller.retry()

    class Factory(private val repository: PassRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PassViewModel(repository) as T
    }
}
