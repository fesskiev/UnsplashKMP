package core.network.photos.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    var city: String? = null,
    var country: String? = null,
    var position: PositionResponse? = null
)