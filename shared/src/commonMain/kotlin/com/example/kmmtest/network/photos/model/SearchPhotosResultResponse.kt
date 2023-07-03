package com.example.kmmtest.network.photos.model

import com.example.kmmtest.network.photos.model.PhotoResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosResultResponse(
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val photos: List<PhotoResponse>
)