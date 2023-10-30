package com.unsplash.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosResultDto(
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val photos: List<PhotoDto>
)