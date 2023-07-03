package com.example.kmmtest.domain

import com.example.kmmtest.network.photos.PhotosNetworkSource
import com.example.kmmtest.network.photos.model.OrderType
import com.example.kmmtest.network.photos.model.PhotoResponse

class GetPhotosUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(page: Int, order: OrderType): Result<List<PhotoResponse>> =
        try {
            val data = networkSource.getPhotos(page, order.type)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
}