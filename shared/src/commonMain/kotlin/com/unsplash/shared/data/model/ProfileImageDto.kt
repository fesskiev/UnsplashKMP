package com.unsplash.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageDto(
    var small: String,
    var medium: String,
    var large: String
)