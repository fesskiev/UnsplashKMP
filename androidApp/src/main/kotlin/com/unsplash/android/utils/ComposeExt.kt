package com.unsplash.android.utils

import androidx.compose.foundation.lazy.grid.LazyGridState

fun LazyGridState.isScrolledToTheEnd(): Boolean =
layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1