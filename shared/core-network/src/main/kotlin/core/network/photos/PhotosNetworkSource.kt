package core.network.photos

import core.network.photos.model.PhotoResponse
import core.network.photos.model.SearchPhotosResultResponse

interface PhotosNetworkSource {
    suspend fun getPhotos(page: Int, orderBy: String): List<PhotoResponse>
    suspend fun getRandomPhotos(): List<PhotoResponse>
    suspend fun getPhoto(id: String): PhotoResponse
    suspend fun searchPhoto(query: String): SearchPhotosResultResponse
}