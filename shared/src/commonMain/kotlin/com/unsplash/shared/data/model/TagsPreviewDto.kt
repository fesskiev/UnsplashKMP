package com.unsplash.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TagsPreviewDto(
    val title: String,
    val type: String
)