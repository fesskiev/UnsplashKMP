package com.example.kmmtest.network.photos.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageResponse(
    var small: String,
    var medium: String,
    var large: String
)