package com.unsplash.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.unsplash.android.navigation.Screen
import com.unsplash.android.navigation.drawerScreens
import com.unsplash.android.navigation.isItemSelected

@Composable
fun NavRail(currentRoute: String?, onItemClick: (Screen) -> Unit) {
    NavigationRail {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            drawerScreens.forEach { screen ->
                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(screen.resId),
                            contentDescription = null
                        )
                    },
                    label = { Text(text = screen.name) },
                    selected = screen.isItemSelected(currentRoute),
                    onClick = { onItemClick(screen) }
                )
            }
        }
    }
}