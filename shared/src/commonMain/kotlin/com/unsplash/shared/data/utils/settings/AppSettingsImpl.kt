package com.unsplash.shared.data.utils.settings

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsImpl(
    private val settings: FlowSettings,
    private val coroutineScope: CoroutineScope,
) : AppSettings {

    override val state: StateFlow<SettingsState?> =
        settings.getStringFlow(themeModeKey, ThemeMode.SYSTEM.toString())
            .map {
                SettingsState(
                    themeMode = ThemeMode.valueOf(it)
                )
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    override fun updateAppThemeMode(themeMode: ThemeMode) {
        coroutineScope.launch {
            settings.putString(
                themeModeKey,
                themeMode.toString()
            )
        }
    }

    companion object {
        const val themeModeKey = "themeModeKey"
    }
}
