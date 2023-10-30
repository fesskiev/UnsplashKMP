package com.unsplash.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("promoted_at")
    val promotedAt: String?,
    val views: Int?  = null,
    val downloads: Int? = null,
    val description: String?,
    @SerialName("alt_description")
    val altDescription: String?,
    val urls: UrlsDto,
    val links: LinksDto,
    val likes: Int,
    val user: UserDto,
    val location: LocationDto? = null,
    @SerialName("tags_preview")
    val tagsPreview: List<TagsPreviewDto>? = null,
)