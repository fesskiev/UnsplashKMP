package com.unsplash.shared.data.remote

import com.unsplash.shared.data.model.PhotoDto
import com.unsplash.shared.data.model.SearchPhotosResultDto
import kotlinx.coroutines.flow.Flow

interface PhotosNetworkSource {
    suspend fun getPhotos(page: Int, orderBy: String): List<PhotoDto>
    suspend fun getRandomPhotos(): List<PhotoDto>
    suspend fun getPhoto(id: String): PhotoDto
    suspend fun searchPhoto(query: String): SearchPhotosResultDto
    fun downloadPhoto(photoUrl: String): Flow<Pair<ByteArray?, Int>>
}