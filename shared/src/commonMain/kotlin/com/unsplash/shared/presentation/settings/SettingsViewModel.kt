package com.unsplash.shared.presentation.settings

import com.unsplash.shared.data.utils.settings.AppSettings
import com.unsplash.shared.data.utils.settings.ThemeMode
import com.unsplash.shared.presentation.viewmodel.BaseViewModel
import com.unsplash.shared.presentation.settings.SettingsContract.Event
import com.unsplash.shared.presentation.settings.SettingsContract.State
import com.unsplash.shared.presentation.settings.SettingsContract.Effect
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val appSettings: AppSettings
) : BaseViewModel<Event, State, Effect>() {

    override val initialState: State = State()

    init {
        viewModelScope.launch {
            appSettings.state.collect {
                setViewState {
                    copy(
                        themeMode = it?.themeMode,
                        error = null
                    )
                }
            }
        }
    }

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.AppThemeChangedAction -> applyAppThemeMode(viewEvent.themeMode)
        }
    }

    private fun applyAppThemeMode(themeMode: ThemeMode) {
        appSettings.updateAppThemeMode(themeMode)
    }
}