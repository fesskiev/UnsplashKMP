package com.unsplash.android.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.unsplash.android.screens.PhotoDetailsScreen
import com.unsplash.android.screens.PhotoListScreen
import com.unsplash.android.screens.SearchPhotosScreen

fun NavGraphBuilder.photosNavGraph(
    isExpandedScreen: Boolean,
    navController: NavHostController,
    onOpenDrawerClick: () -> Unit,
    onLogout: () -> Unit
) {
    navigation(
        startDestination = PhotosGraph.PhotoList.route,
        route = HomeGraph.Photos.route
    ) {
        composable(PhotosGraph.PhotoList.route) {
            PhotoListScreen(
                modifier = Modifier,
                isExpandedScreen = isExpandedScreen,
                photoDetailsScreen = { photoId ->
                    PhotoDetailsScreen(
                        photoId,
                        onLogout = onLogout
                    )
                },
                onSearchClick = {
                    navController.navigateToRoute(PhotosGraph.PhotosSearch.route)
                },
                onPhotoDetailsClick = {
                    navController.navigateToRoute("$PHOTO_DETAILS_SCREEN/$it")
                },
                onOpenDrawerClick = onOpenDrawerClick,
                onLogout = onLogout
            )
        }
        composable(PhotosGraph.PhotosSearch.route) {
            SearchPhotosScreen(
                isExpandedScreen = isExpandedScreen,
                onPhotoDetailsClick = {
                    navController.navigateToRoute("$PHOTO_DETAILS_SCREEN/$it")
                },
                onLogout = onLogout
            )
        }
        composable(
            route = PhotosGraph.PhotoDetails.route,
            arguments = listOf(navArgument("photoId") { type = NavType.StringType }),
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("photoId")?.let { photoId ->
                PhotoDetailsScreen(
                    photoId,
                    onLogout = onLogout
                )
            }
        }
    }
}