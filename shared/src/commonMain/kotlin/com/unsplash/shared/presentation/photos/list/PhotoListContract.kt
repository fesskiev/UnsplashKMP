package com.unsplash.shared.presentation.photos.list

import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.presentation.common.PagingState
import com.unsplash.shared.presentation.viewmodel.ViewEvent
import com.unsplash.shared.presentation.viewmodel.ViewSideEffect
import com.unsplash.shared.presentation.viewmodel.ViewState

class PhotoListContract {

    data class State(
        val isLoading: Boolean = false,
        val responseError: Throwable? = null,
        val isRefresh: Boolean = false,
        val photos: List<Photo> = listOf(),
        val orderType: OrderType = OrderType.LATEST,
        val paging: PagingState = PagingState()
    ) : ViewState {

        override fun toString(): String {
            return "PL(isLoading=$isLoading, error=$responseError, isRefresh=$isRefresh, photos=${photos.size}, orderType=$orderType, paging=$paging)"
        }
    }

    sealed class Event : ViewEvent {
        object GetFirstPhotosPageAction : Event()
        object LoadMorePhotosAction : Event()
        object RefreshPhotosAction : Event()
        object RetryFetchPhotosAction : Event()
        data class OrderPhotosAction(val orderType: OrderType) : Event()
        data class PhotoClickAction(val photoId: String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        data class ShowPhotoDetailsScreen(val photoId: String) : Effect()
        object ShowLoginScreen : Effect()
    }
}