package core.network.photos.model

import kotlinx.serialization.Serializable

@Serializable
data class PositionResponse(
    var latitude: Float? = null,
    var longitude: Float? = null
)