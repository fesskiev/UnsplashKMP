package com.unsplash.shared.data.utils

import com.unsplash.shared.data.remote.network.client.exceptions.AuthException
import com.unsplash.shared.data.utils.download.PhotoDownloadException

inline fun <T : Any> Result<T>.onLogout(action: () -> Unit): Result<T> {
    if (this is Result.Error && this.error is AuthException) {
        action()
    }
    return this
}

inline fun <T : Any> Result<T>.onDownloadFailure(action: (PhotoDownloadException) -> Unit): Result<T> {
    if (this is Result.Error && this.error is PhotoDownloadException) {
        action(this.error)
    }
    return this
}

inline fun <T : Any> Result<T>.onResponseFailure(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(this.error)
    }
    return this
}

