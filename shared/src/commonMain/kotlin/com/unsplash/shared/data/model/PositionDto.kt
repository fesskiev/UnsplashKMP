package com.unsplash.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PositionDto(
    var latitude: Double? = null,
    var longitude: Double? = null
)