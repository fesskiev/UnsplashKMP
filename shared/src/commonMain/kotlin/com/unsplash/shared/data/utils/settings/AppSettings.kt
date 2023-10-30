package com.unsplash.shared.data.utils.settings

import kotlinx.coroutines.flow.StateFlow

interface AppSettings {

    val state: StateFlow<SettingsState?>

    fun updateAppThemeMode(themeMode: ThemeMode)
}