package com.example.kmmtest.domain

import com.example.kmmtest.network.photos.PhotosNetworkSource
import com.example.kmmtest.network.photos.model.PhotoResponse

class GetPhotoUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(id: String): Result<PhotoResponse> =
        try {
            val data = networkSource.getPhoto(id)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
}