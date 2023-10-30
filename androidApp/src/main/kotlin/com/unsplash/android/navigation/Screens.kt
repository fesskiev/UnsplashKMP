package com.unsplash.android.navigation

import androidx.annotation.DrawableRes
import com.unsplash.android.R

const val PHOTO_DETAILS_SCREEN = "photo_details_screen"

sealed class Screen(
    val route: String,
    val name: String,
    @DrawableRes val resourceId: Int?
) {
    val resId: Int
        get() = resourceId ?: throw IllegalStateException("resource id must not be null")
}

fun Screen.isItemSelected(currentRoute: String?) : Boolean {
    if (currentRoute == null) {
        return false
    }
    if (currentRoute == PhotosGraph.PhotoList.route) {
        return route == HomeGraph.Photos.route
    }
    if (currentRoute == SettingsGraph.Settings.route) {
        return route == HomeGraph.Settings.route
    }
    return false
}

object HomeGraph {

    object Photos :
        Screen(
            route = "photos_home_screen",
            name = "Photos",
            resourceId = R.drawable.ic_photo
        )

    object Settings :
        Screen(
            route = "settings_home_screen",
            name = "Settings",
            resourceId = R.drawable.ic_settings
        )
}

object PhotosGraph {
    object PhotoList :
        Screen(
            route = "photo_list_screen",
            name = "Photos",
            resourceId = null
        )

    object PhotosSearch :
        Screen(
            route = "photos_search_screen",
            name = "Search photos",
            resourceId = null
        )

    object PhotoDetails :
        Screen(
            route = "$PHOTO_DETAILS_SCREEN/{photoId}",
            name = "Photo Details",
            resourceId = null
        )
}

object SettingsGraph {
    object Settings :
        Screen(
            route = "settings_screen",
            name = "Settings",
            resourceId = null
        )
}

val drawerScreens = listOf(HomeGraph.Photos, HomeGraph.Settings)