package com.unsplash.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinksDto(
    val self: String,
    val html: String,
    val download: String,
    @SerialName("download_location")
    val downloadLocation: String
)