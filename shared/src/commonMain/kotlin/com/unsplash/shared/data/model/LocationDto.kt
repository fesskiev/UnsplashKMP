package com.unsplash.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    var name: String? = null,
    var city: String? = null,
    var country: String? = null,
    var position: PositionDto? = null
)