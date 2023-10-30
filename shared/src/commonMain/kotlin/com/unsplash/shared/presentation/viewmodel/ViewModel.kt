package com.unsplash.shared.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {

    val viewModelScope: CoroutineScope

    protected open fun onCleared()
}