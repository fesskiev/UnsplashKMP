package com.unsplash.shared.presentation.photos.search

import com.unsplash.shared.domain.model.SearchResult
import com.unsplash.shared.presentation.common.PagingState
import com.unsplash.shared.presentation.viewmodel.ViewEvent
import com.unsplash.shared.presentation.viewmodel.ViewSideEffect
import com.unsplash.shared.presentation.viewmodel.ViewState

class PhotosSearchContract {

    data class State(
        val isLoading: Boolean = false,
        val responseError: Throwable? = null,
        val suggestions: ArrayDeque<String> = ArrayDeque(),
        val filteredSuggestions: List<String> = ArrayDeque(),
        val query: String = "",
        val searchResult: SearchResult? = null,
        val paging: PagingState = PagingState()
    ): ViewState

    sealed class Event : ViewEvent {
        object RetrySearchPhotosAction : Event()
        data class QueryChangeAction(val query: String) : Event()
        data class PhotoClickAction(val photoId: String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ShowLoginScreen : Effect()
        data class ShowPhotoDetailsScreen(val photoId: String) : Effect()
    }
}

