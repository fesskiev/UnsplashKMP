package com.unsplash.shared.presentation.viewmodel

import kotlinx.coroutines.*

actual abstract class ViewModel {

    actual val viewModelScope: CoroutineScope by lazy {
        return@lazy CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }
}
