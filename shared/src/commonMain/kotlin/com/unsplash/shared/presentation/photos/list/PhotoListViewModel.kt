package com.unsplash.shared.presentation.photos.list

import com.unsplash.shared.data.utils.onResponseFailure
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.data.utils.onFailure
import com.unsplash.shared.data.utils.onLogout
import com.unsplash.shared.data.utils.onSuccess
import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.domain.usecases.GetPhotosUseCase
import com.unsplash.shared.presentation.viewmodel.BaseViewModel
import com.unsplash.shared.presentation.common.PagingState
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Event
import com.unsplash.shared.presentation.photos.list.PhotoListContract.State
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Effect
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Effect.ShowPhotoDetailsScreen
import com.unsplash.shared.domain.usecases.RefreshPhotosUseCase
import kotlinx.coroutines.launch

class PhotoListViewModel(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val refreshPhotosUseCase: RefreshPhotosUseCase
) : BaseViewModel<Event, State, Effect>() {

    override val initialState: State = State()

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.GetFirstPhotosPageAction -> getGetFirstPhotosPage()
            is Event.PhotoClickAction -> openPhotoDetailsScreen(viewEvent.photoId)
            is Event.LoadMorePhotosAction -> loadMorePhotos()
            is Event.OrderPhotosAction -> orderPhotos(viewEvent.orderType)
            is Event.RefreshPhotosAction -> refreshPhotos()
            is Event.RetryFetchPhotosAction -> retryFetchPhotos()
        }
    }

    private fun retryFetchPhotos() {
        viewModelScope.launch {
            showProgress()
            val orderType = viewState.value.orderType
            val page = viewState.value.paging.page
            getPhotosUseCase.invoke(page, orderType)
                .onSuccess { showPhotos(it) }
                .onLogout { logout() }
                .onResponseFailure { showError(it) }
        }
    }

    private fun getGetFirstPhotosPage() {
        val photos = viewState.value.photos
        if (photos.isEmpty()) {
            viewModelScope.launch {
                showProgress()
                val orderType = viewState.value.orderType
                getPhotosUseCase.invoke(1, orderType)
                    .onSuccess { showPhotos(it) }
                    .onLogout { logout() }
                    .onResponseFailure { showError(it) }
            }
        }
    }

    private fun loadMorePhotos() {
        val pagingState = viewState.value.paging
        if (!pagingState.endOfPaginationReached && !pagingState.loadMore) {
            viewModelScope.launch {
                showPagingProgress()
                val orderType = viewState.value.orderType
                val page = viewState.value.paging.page
                getPhotosUseCase.invoke(page + 1, orderType)
                    .onSuccess { addPhotosPage(it) }
                    .onLogout { logout() }
                    .onFailure { showPagingError(it) }
            }
        }
    }

    private fun orderPhotos(orderType: OrderType) {
        viewModelScope.launch {
            reorderPhotoList(orderType)
            refreshPhotosUseCase.invoke(orderType)
                .onSuccess { showPhotoList(it) }
                .onLogout { logout() }
                .onResponseFailure { showError(it) }
        }
    }

    private fun refreshPhotos() {
        viewModelScope.launch {
            refreshPhotoList()
            val orderType = viewState.value.orderType
            refreshPhotosUseCase.invoke(orderType)
                .onSuccess { showPhotoList(it) }
                .onLogout { logout() }
                .onResponseFailure { showError(it) }
        }
    }

    private fun showPhotoList(photos: List<Photo>) {
        setViewState {
            copy(
                isLoading = false,
                responseError = null,
                isRefresh = false,
                photos = photos
            )
        }
    }

    private fun reorderPhotoList(orderType: OrderType) {
        setViewState {
            copy(
                isLoading = true,
                responseError = null,
                photos = listOf(),
                paging = PagingState(),
                orderType = orderType
            )
        }
    }

    private fun refreshPhotoList() {
        setViewState {
            copy(
                isLoading = false,
                responseError = null,
                isRefresh = true,
                photos = listOf(),
                paging = PagingState()
            )
        }
    }

    private fun openPhotoDetailsScreen(photoId: String) {
        setViewEffect {
            ShowPhotoDetailsScreen(photoId)
        }
    }

    private fun addPhotosPage(newPhotos: List<Photo>) {
        setViewState {
            copy(
                paging = paging.copy(
                    loadMore = false,
                    endOfPaginationReached = newPhotos.isEmpty(),
                    page = paging.page + 1,
                    error = null
                ),
                photos = photos.plus(newPhotos)
            )
        }
    }

    private fun showPagingError(throwable: Throwable) {
        setViewState {
            copy(
                paging = paging.copy(
                    loadMore = false,
                    error = throwable
                )
            )
        }
    }

    private fun showPagingProgress() {
        setViewState {
            copy(
                paging = paging.copy(
                    loadMore = true,
                    error = null
                )
            )
        }
    }

    private fun showPhotos(photos: List<Photo>) {
        setViewState {
            copy(
                isLoading = false,
                responseError = null,
                photos = photos
            )
        }
    }

    private fun showError(throwable: Throwable) {
        setViewState {
            copy(
                isLoading = false,
                responseError = throwable
            )
        }
    }

    private fun showProgress() {
        setViewState {
            copy(
                isLoading = true,
                responseError = null
            )
        }
    }

    private fun logout() {
        setViewEffect {
            Effect.ShowLoginScreen
        }
    }
}
