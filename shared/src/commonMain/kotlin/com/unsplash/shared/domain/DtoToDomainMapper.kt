package com.unsplash.shared.domain

import com.unsplash.shared.data.model.PhotoDto
import com.unsplash.shared.data.model.SearchPhotosResultDto
import com.unsplash.shared.domain.model.Location
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.domain.model.SearchResult
import com.unsplash.shared.domain.model.TagsPreview
import com.unsplash.shared.domain.model.Urls
import com.unsplash.shared.domain.model.User

fun PhotoDto.toPhoto() = Photo(
    id = id,
    createdAt = createdAt,
    views = views,
    downloads = downloads,
    likes = likes,
    description = description,
    altDescription = altDescription,
    urls = Urls(
        small = urls.small,
        regular = urls.regular,
        full = urls.full)
    ,
    user = User(name = user.name),
    location = Location(
        name = location?.name,
        city = location?.city,
        country = location?.country,
        latitude = location?.position?.latitude,
        longitude = location?.position?.longitude
    ),
    tagsPreview = tagsPreview?.map {
        TagsPreview(
            title = it.title,
            type = it.type
        )
    }
)

fun SearchPhotosResultDto.toSearchResult(): SearchResult =
    SearchResult(
        total = total,
        totalPages = totalPages,
        photos = photos.map { it.toPhoto() }
    )