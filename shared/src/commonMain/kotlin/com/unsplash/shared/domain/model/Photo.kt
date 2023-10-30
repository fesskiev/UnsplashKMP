package com.unsplash.shared.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Photo(
    val id: String,
    val createdAt: String,
    val views: Int? = null,
    val downloads: Int? = null,
    val description: String? = null,
    val altDescription: String? = null,
    val likes: Int,
    val urls: Urls,
    val user: User,
    val location: Location? = null,
    val tagsPreview: List<TagsPreview>? = null,
) {
    val createdDate: String
        get()  {
            val localDateTime = Instant.parse(createdAt).toLocalDateTime(TimeZone.UTC)
            return localDateTime.year.toString() + "." + localDateTime.monthNumber.toString()
        }
}