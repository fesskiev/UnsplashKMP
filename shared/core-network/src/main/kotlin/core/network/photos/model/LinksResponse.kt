package core.network.photos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinksResponse(
    val self: String,
    val html: String,
    val download: String,
    @SerialName("download_location")
    val downloadLocation: String
)