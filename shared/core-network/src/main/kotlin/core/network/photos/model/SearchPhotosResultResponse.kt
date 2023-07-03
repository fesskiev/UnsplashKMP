package core.network.photos.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosResultResponse(
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("results")
    val photos: List<PhotoResponse>
)