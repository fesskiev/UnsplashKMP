package com.unsplash.shared.domain.model

enum class OrderType(val type: String) {
    LATEST("latest"), OLDEST("oldest"), POPULAR("popular")
}