package com.example.kmmtest.network.photos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UrlsResponse(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String,
    @SerialName("small_s3")
    val smallS3: String
)