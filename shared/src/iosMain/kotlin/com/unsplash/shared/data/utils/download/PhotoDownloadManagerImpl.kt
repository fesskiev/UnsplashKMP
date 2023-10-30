package com.unsplash.shared.data.utils.download

import com.unsplash.shared.data.utils.Result

class PhotoDownloadManagerImpl : PhotoDownloadManager {

    override suspend fun downloadPhoto(
        url: String,
        title: String?,
        message: String?
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}