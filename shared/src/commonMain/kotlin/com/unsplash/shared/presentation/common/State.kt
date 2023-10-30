package com.unsplash.shared.presentation.common

data class PagingState(
    val loadMore: Boolean = false,
    val error: Throwable? = null,
    val page: Int = 1,
    val endOfPaginationReached: Boolean = false
)