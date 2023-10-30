package com.unsplash.shared.presentation.photos.details

import com.unsplash.shared.data.utils.download.PhotoDownloadException
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.presentation.viewmodel.ViewEvent
import com.unsplash.shared.presentation.viewmodel.ViewSideEffect
import com.unsplash.shared.presentation.viewmodel.ViewState
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.PhotoDownloadState
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.State

class PhotoDetailsContract {

    data class State(
        val isLoading: Boolean = false,
        val isRefresh: Boolean = false,
        val photoDownloadState: PhotoDownloadState = PhotoDownloadState.Idle,
        val responseError: Throwable? = null,
        val photo: Photo? = null
    ): ViewState

    sealed class PhotoDownloadState {
        object Idle : PhotoDownloadState()
        object Downloading : PhotoDownloadState()
        object Downloaded : PhotoDownloadState()
        data class Error(val exception: PhotoDownloadException?) : PhotoDownloadState()
    }

    sealed class Event : ViewEvent {
        object RefreshPhotoAction : Event()
        object RetryFetchPhotoAction : Event()
        object DownloadPhotoAction : Event()
        object ErrorShackBarDismissAction : Event()
        data class PhotoLoadErrorAction(val throwable: Throwable) : Event()
        data class GetDetailedPhotoAction(val id: String) : Event()
    }

    sealed class Effect : ViewSideEffect {
        object ShowLoginScreen : Effect()
    }
}

fun State.isDownloading() = this.photoDownloadState is PhotoDownloadState.Downloading

fun State.isDownloaded() = this.photoDownloadState is PhotoDownloadState.Downloaded
