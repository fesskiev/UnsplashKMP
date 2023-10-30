package com.unsplash.shared.domain.model

data class Location(
    var name: String? = null,
    var city: String? = null,
    var country: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
)