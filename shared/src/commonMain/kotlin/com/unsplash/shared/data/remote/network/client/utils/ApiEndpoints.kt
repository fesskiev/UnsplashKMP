package com.unsplash.shared.data.remote.network.client.utils

enum class ApiEndpoints(val url: String) {
    BASE("api.unsplash.com"),
    PHOTOS("/photos"),
    PHOTOS_RANDOM("/photos/random"),
    PHOTOS_SEARCH("/search/photos")
}