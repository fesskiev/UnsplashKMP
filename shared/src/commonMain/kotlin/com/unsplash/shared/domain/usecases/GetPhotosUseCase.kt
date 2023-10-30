package com.unsplash.shared.domain.usecases

import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.data.utils.Result
import com.unsplash.shared.data.utils.dataSourceStrategy
import com.unsplash.shared.domain.toPhoto

class GetPhotosUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(page: Int, order: OrderType): Result<List<Photo>> =
        // TODO add cache for photos
        dataSourceStrategy(
            getLocalData = {
                validateArguments(page)
                emptyList()
            },
            getRemoteData = {
                validateArguments(page)
                networkSource.getPhotos(page, order.type).map { it.toPhoto() }
            },
            saveLocalData = {

            }
        )

    private fun validateArguments(page: Int) {
        if (page < 1) {
            throw IllegalArgumentException("page must start with 1")
        }
    }
}