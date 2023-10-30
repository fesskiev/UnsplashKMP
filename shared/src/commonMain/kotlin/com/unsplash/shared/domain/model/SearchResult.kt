package com.unsplash.shared.domain.model

data class SearchResult(
    val total: Int,
    val totalPages: Int,
    val photos: List<Photo>
)