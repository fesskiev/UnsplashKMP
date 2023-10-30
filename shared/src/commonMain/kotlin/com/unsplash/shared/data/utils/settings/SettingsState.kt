package com.unsplash.shared.data.utils.settings

data class SettingsState(
    val themeMode: ThemeMode
)

enum class ThemeMode {
    DAY,
    NIGHT,
    SYSTEM
}