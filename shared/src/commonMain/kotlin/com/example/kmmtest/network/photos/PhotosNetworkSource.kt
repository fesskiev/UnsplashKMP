package com.example.kmmtest.network.photos

import com.example.kmmtest.network.photos.model.PhotoResponse
import com.example.kmmtest.network.photos.model.SearchPhotosResultResponse

interface PhotosNetworkSource {
    suspend fun getPhotos(page: Int, orderBy: String): List<PhotoResponse>
    suspend fun getRandomPhotos(): List<PhotoResponse>
    suspend fun getPhoto(id: String): PhotoResponse
    suspend fun searchPhoto(query: String): SearchPhotosResultResponse
}