package com.unsplash.shared.data.utils.download

import com.unsplash.shared.data.utils.Result

interface PhotoDownloadManager {

    suspend fun downloadPhoto(url: String, title: String?, message: String?) : Result<Unit>
}