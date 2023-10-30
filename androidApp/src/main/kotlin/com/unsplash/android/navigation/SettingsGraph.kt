package com.unsplash.android.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.unsplash.android.screens.SettingsScreen

fun NavGraphBuilder.settingsNavHost(
    isExpandedScreen: Boolean,
    onOpenDrawerClick: () -> Unit
) {
    navigation(
        startDestination = SettingsGraph.Settings.route,
        route = HomeGraph.Settings.route
    ) {
        composable(SettingsGraph.Settings.route) {
            SettingsScreen(
                isExpandedScreen,
                onOpenDrawerClick
            )
        }
    }
}