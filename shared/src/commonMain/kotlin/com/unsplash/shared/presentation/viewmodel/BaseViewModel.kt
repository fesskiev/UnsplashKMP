package com.unsplash.shared.presentation.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface ViewEvent

interface ViewState

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, State : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    abstract val initialState: State

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    val viewState: StateFlow<State> by lazy { _viewState }

    private val _viewEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    private val viewEvent: SharedFlow<Event> = _viewEvent

    private val _viewEffect: MutableSharedFlow<Effect> by lazy { MutableSharedFlow() }
    val viewEffect: SharedFlow<Effect> = _viewEffect

    init {
        collectViewEvents()
    }

    fun sendViewEvent(event: Event) {
        viewModelScope.launch {
            _viewEvent.emit(event)
        }
    }

    fun setViewState(reducer: State.() -> State) {
        val newState = viewState.value.reducer()
        println("STATE: $newState")
        _viewState.value = newState
    }

    fun setViewEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch {
            _viewEffect.emit(effectValue)
        }
    }

    private fun collectViewEvents() {
        viewModelScope.launch {
            viewEvent.collect {
                handleViewEvents(it)
            }
        }
    }

    abstract fun handleViewEvents(viewEvent: Event)
}
