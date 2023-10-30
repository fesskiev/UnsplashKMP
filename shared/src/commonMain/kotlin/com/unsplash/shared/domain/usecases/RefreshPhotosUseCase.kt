package com.unsplash.shared.domain.usecases

import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.data.utils.getResult
import com.unsplash.shared.data.utils.Result
import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.domain.toPhoto

class RefreshPhotosUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(order: OrderType): Result<List<Photo>> = getResult {
         networkSource.getPhotos(page = 1, orderBy = order.type).map { it.toPhoto() }
    }
}