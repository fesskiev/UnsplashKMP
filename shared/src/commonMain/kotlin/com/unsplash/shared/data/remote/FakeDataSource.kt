package com.unsplash.shared.data.remote

import com.unsplash.shared.data.model.LinksDto
import com.unsplash.shared.data.model.LocationDto
import com.unsplash.shared.data.model.PhotoDto
import com.unsplash.shared.data.model.PositionDto
import com.unsplash.shared.data.model.ProfileImageDto
import com.unsplash.shared.data.model.TagsPreviewDto
import com.unsplash.shared.data.model.UrlsDto
import com.unsplash.shared.data.model.UserDto
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.domain.toPhoto

fun generateFakePhotoDtoList(): List<PhotoDto> {
    val photoList = mutableListOf<PhotoDto>()
    repeat(10) { i ->
        val photo = PhotoDto(
            id = "photo_$i",
            createdAt = "2023-09-15T12:00:00Z",
            updatedAt = "2023-09-15T12:30:00Z",
            promotedAt = null,
            views = (1000..5000).random(),
            downloads = (500..2000).random(),
            description = "A beautiful photo $i",
            altDescription = "Alt description for photo $i",
            urls = UrlsDto(
                raw = "https://example.com/photo$i/raw",
                full = "https://example.com/photo$i/full",
                regular = "https://example.com/photo$i/regular",
                small = "https://example.com/photo$i/small",
                thumb = "https://example.com/photo$i/thumb",
                smallS3 = "https://example.com/photo$i/smallS3"
            ),
            links = LinksDto(
                self = "https://example.com/photo/$i",
                html = "https://example.com/photo/$i/html",
                download = "https://example.com/photo/$i/download",
                downloadLocation = "location $i"
            ),
            likes = (100..500).random(),
            user = UserDto(
                id = "user_$i",
                updatedAt = "2023-09-15T12:30:00Z",
                username = "photographer$i",
                name = "Photographer $i",
                firstName = "Photographer $i",
                lastName = "Photographer $i",
                bio = "Bio $i",
                location = "Location $i",
                profileImage = ProfileImageDto(
                    small = "https://example.com/photo$i/small",
                    medium = "https://example.com/photo$i/medium",
                    large = "https://example.com/photo$i/large"
                ),
                totalCollections = (1000..5000).random(),
                totalLikes = (1000..5000).random(),
                totalPhotos = (1000..5000).random(),
            ),
            location = LocationDto(
                name = "Location $i",
                city = "City $i",
                country = "Country $i",
                position = PositionDto(
                    latitude = (30..40).random().toDouble(),
                    longitude =  (30..40).random().toDouble()
                )
            ),
            tagsPreview = listOf(
                TagsPreviewDto(
                    title = "tag1",
                    type = "type1"
                ),
                TagsPreviewDto(
                    title = "tag2",
                    type = "type2"
                )
            )
        )

        photoList.add(photo)
    }
    return photoList
}

fun generateFakePhoto(): Photo = generateFakePhotoDtoList().first().toPhoto()