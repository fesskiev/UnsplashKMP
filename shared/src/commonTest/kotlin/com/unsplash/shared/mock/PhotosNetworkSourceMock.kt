package com.unsplash.shared.mock

import com.unsplash.shared.data.model.PhotoDto
import com.unsplash.shared.data.model.SearchPhotosResultDto
import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.data.remote.generateFakePhotoDtoList
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow

class PhotosNetworkSourceMock : PhotosNetworkSource {

    private val photos = generateFakePhotoDtoList()

    var isSuccessResponse = true

    override suspend fun getPhotos(page: Int, orderBy: String): List<PhotoDto> {
        if (isSuccessResponse) {
            return photos
        }
        throw IOException("Network error")
    }

    override suspend fun getRandomPhotos(): List<PhotoDto> {
        if (isSuccessResponse) {
            return photos.shuffled()
        }
        throw IOException("Network error")
    }

    override suspend fun getPhoto(id: String): PhotoDto {
        if (isSuccessResponse) {
            return photos.random()
        }
        throw IOException("Network error")
    }

    override suspend fun searchPhoto(query: String): SearchPhotosResultDto {
        TODO("Not yet implemented")
    }

    override fun downloadPhoto(photoUrl: String): Flow<Pair<ByteArray?, Int>> {
        TODO("Not yet implemented")
    }
}