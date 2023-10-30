@file:OptIn(ExperimentalMaterial3Api::class)

package com.unsplash.android.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unsplash.android.R
import com.unsplash.android.theme.AppTheme
import com.unsplash.shared.domain.model.OrderType
import java.util.Locale

val orderItems = listOf(OrderType.LATEST, OrderType.OLDEST, OrderType.POPULAR)

@Composable
fun PhotoListTopBar(
    isExpandedScreen: Boolean,
    selectedOrderType: OrderType,
    onOrderClick: (OrderType) -> Unit,
    onSearchClick: () -> Unit,
    onOpenDrawerClick: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth =
        if (isExpandedScreen) (configuration.screenWidthDp - 100).dp / 2 else configuration.screenWidthDp.dp

    TopAppBar(
        modifier = Modifier.width(screenWidth),
        title = {

        },
        navigationIcon = {
            if (!isExpandedScreen) {
                IconButton(onClick = { onOpenDrawerClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painterResource(id = R.drawable.ic_search),
                    contentDescription = ""
                )
            }
            OrderPhotosMenu(
                selectedOrderType = selectedOrderType,
                onOrderClick = onOrderClick
            )
        }
    )
}

@Composable
private fun OrderPhotosMenu(selectedOrderType: OrderType, onOrderClick: (OrderType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painterResource(id = R.drawable.ic_filter),
                contentDescription = ""
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }
        ) {
            orderItems.forEach { item ->
                DropdownMenuItem(
                    leadingIcon = {
                        if (selectedOrderType == item) {
                            Icon(
                                painterResource(id = R.drawable.ic_check),
                                contentDescription = ""
                            )
                        }
                    },
                    text = { Text(text = item.type.uppercase(Locale.ROOT)) },
                    onClick = {
                        onOrderClick(item)
                        expanded = !expanded
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsTopAppBar(
    isExpandedScreen: Boolean,
    onOpenDrawerClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Settings") },
        navigationIcon = {
            if (!isExpandedScreen) {
                IconButton(onClick = { onOpenDrawerClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null
                    )
                }
            }
        },
    )
}

@Preview
@Composable
fun TopBarPreview() {
    AppTheme {
        PhotoListTopBar(
            isExpandedScreen = true,
            selectedOrderType = OrderType.LATEST,
            onOrderClick = {},
            onSearchClick = {},
            onOpenDrawerClick = {})
    }
}