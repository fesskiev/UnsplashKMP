package com.unsplash.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.unsplash.android.components.NavRail
import com.unsplash.android.navigation.HomeGraph
import com.unsplash.android.navigation.PhotosGraph
import com.unsplash.android.navigation.currentRoute
import com.unsplash.android.navigation.drawerScreens
import com.unsplash.android.navigation.isItemSelected
import com.unsplash.android.navigation.navigateToItemScreen
import com.unsplash.android.navigation.photosNavGraph
import com.unsplash.android.navigation.settingsNavHost
import com.unsplash.android.utils.LogCompositions
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(isExpandedScreen: Boolean) {
    LogCompositions("HomeScreen")

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val currentRoute = navController.currentRoute()
    println("current route: $currentRoute")
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != PhotosGraph.PhotoDetails.route && !isExpandedScreen,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                drawerScreens.forEach { screen ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(screen.resId),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = screen.name) },
                        selected = screen.isItemSelected(currentRoute),
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigateToItemScreen(screen)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Row(Modifier.fillMaxSize()) {
                if (isExpandedScreen) {
                    NavRail(
                        currentRoute = currentRoute,
                        onItemClick = {
                            scope.launch { drawerState.close() }
                            navController.navigateToItemScreen(it)
                        }
                    )
                }
                NavHost(
                    navController = navController,
                    startDestination = HomeGraph.Photos.route
                ) {
                    photosNavGraph(
                        isExpandedScreen,
                        navController,
                        onOpenDrawerClick = { scope.launch { drawerState.open() } },
                        onLogout = { }
                    )
                    settingsNavHost(
                        isExpandedScreen,
                        onOpenDrawerClick = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
    )
}