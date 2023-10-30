package com.unsplash.shared.presentation.photos.details

import com.unsplash.shared.data.utils.download.PhotoDownloadException
import com.unsplash.shared.data.utils.download.PhotoDownloadManager
import com.unsplash.shared.data.utils.onDownloadFailure
import com.unsplash.shared.data.utils.onResponseFailure
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.data.utils.onLogout
import com.unsplash.shared.data.utils.onSuccess
import com.unsplash.shared.domain.usecases.GetPhotoUseCase
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.State
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.Event
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.Effect
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.PhotoDownloadState
import com.unsplash.shared.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class PhotoDetailsViewModel(
    private val getPhotoUseCase: GetPhotoUseCase,
    private val downloadManager: PhotoDownloadManager
) : BaseViewModel<Event, State, Effect>() {

    override val initialState: State = State()

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.GetDetailedPhotoAction -> getDetailedPhoto(viewEvent.id)
            is Event.PhotoLoadErrorAction -> showError(viewEvent.throwable)
            is Event.RefreshPhotoAction -> refreshPhoto()
            is Event.RetryFetchPhotoAction -> refreshPhoto()
            is Event.DownloadPhotoAction -> downloadPhoto()
            is Event.ErrorShackBarDismissAction -> changeDownloadPhotoStateToIdle()
        }
    }

    private fun downloadPhoto() {
        val photo = viewState.value.photo
        val url = photo?.urls?.full
        if (url != null) {
            hideDownloadButton()
            viewModelScope.launch {
                downloadManager.downloadPhoto(
                    url,
                    title = photo.user.name,
                    message = photo.description
                )
                    .onSuccess { showDownloadButton() }
                    .onDownloadFailure { showDownloadError(it) }
            }
        }
    }

    private fun refreshPhoto() {
        viewModelScope.launch {
            refreshPhotoList()
            val photo = viewState.value.photo
            val photoId = photo?.id
            if (photoId != null) {
                getPhotoUseCase.invoke(photoId)
                    .onSuccess { showDetailedPhoto(it) }
                    .onLogout { logout() }
                    .onResponseFailure { showError(it) }
            }
        }
    }

    private fun getDetailedPhoto(id: String) {
        val photo = viewState.value.photo
        if (photo == null || photo.id != id) {
            viewModelScope.launch {
                showProgress()
                getPhotoUseCase.invoke(id)
                    .onSuccess { showDetailedPhoto(it) }
                    .onLogout { logout() }
                    .onResponseFailure { showError(it) }
            }
        }
    }

    private fun changeDownloadPhotoStateToIdle() {
        setViewState {
            copy(
                photoDownloadState = PhotoDownloadState.Idle
            )
        }
    }

    private fun showDownloadError(exception: PhotoDownloadException) {
        setViewState {
            copy(
                photoDownloadState = PhotoDownloadState.Error(exception)
            )
        }
    }

    private fun showDownloadButton() {
        setViewState {
            copy(
                photoDownloadState = PhotoDownloadState.Downloaded
            )
        }
    }

    private fun hideDownloadButton() {
        setViewState {
            copy(
                photoDownloadState = PhotoDownloadState.Downloading
            )
        }
    }

    private fun refreshPhotoList() {
        setViewState {
            copy(
                isLoading = false,
                isRefresh = true,
                responseError = null,
                photo = null
            )
        }
    }

    private fun showDetailedPhoto(photo: Photo) {
        setViewState {
            copy(
                isLoading = false,
                responseError = null,
                photo = photo
            )
        }
    }

    private fun showError(responseError: Throwable) {
        setViewState {
            copy(
                isLoading = false,
                responseError = responseError
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