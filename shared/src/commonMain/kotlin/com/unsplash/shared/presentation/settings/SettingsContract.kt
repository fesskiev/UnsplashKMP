package com.unsplash.shared.presentation.settings

import com.unsplash.shared.data.utils.settings.ThemeMode
import com.unsplash.shared.presentation.viewmodel.ViewEvent
import com.unsplash.shared.presentation.viewmodel.ViewSideEffect
import com.unsplash.shared.presentation.viewmodel.ViewState

class SettingsContract {

    data class State(
        val themeMode: ThemeMode? = null,
        val error: Throwable? = null,
    ): ViewState

    sealed class Event : ViewEvent {
        data class AppThemeChangedAction(val themeMode: ThemeMode) : Event()
    }

    sealed class Effect : ViewSideEffect
}