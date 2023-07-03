package com.example.kmmtest.network.photos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("promoted_at")
    val promotedAt: String?,
    val width: Int,
    val height: Int,
    val description: String?,
    val urls: UrlsResponse,
    val links: LinksResponse,
    val likes: Int,
    val user: UserResponse,
    val location: LocationResponse? = null
)