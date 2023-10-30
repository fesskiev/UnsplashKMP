package com.unsplash.shared.domain.usecases

import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.data.utils.Result
import com.unsplash.shared.data.utils.getResult
import com.unsplash.shared.domain.toPhoto

class GetPhotoUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(id: String): Result<Photo> = getResult {
        if (id.isEmpty() || id.isBlank()) {
            throw IllegalArgumentException("photo id must not be empty or blank")
        }
        networkSource.getPhoto(id).toPhoto()
    }
}