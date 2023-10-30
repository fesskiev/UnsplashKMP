package com.unsplash.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName("updated_at")
    val updatedAt: String,
    val username: String,
    val name: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String?,
    val bio: String?,
    val location: String?,
    @SerialName("profile_image")
    val profileImage: ProfileImageDto,
    @SerialName("total_collections")
    val totalCollections: Int,
    @SerialName("total_likes")
    val totalLikes: Int,
    @SerialName("total_photos")
    val totalPhotos: Int
)