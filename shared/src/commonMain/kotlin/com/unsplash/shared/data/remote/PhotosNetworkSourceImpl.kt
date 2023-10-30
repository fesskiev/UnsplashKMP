package com.unsplash.shared.data.remote

import com.unsplash.shared.data.model.PhotoDto
import com.unsplash.shared.data.model.SearchPhotosResultDto
import com.unsplash.shared.data.remote.network.client.utils.ApiEndpoints
import com.unsplash.shared.data.remote.network.client.utils.CLIENT_ID
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.onDownload
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class PhotosNetworkSourceImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher
) : PhotosNetworkSource {

    override suspend fun getPhotos(page: Int, orderBy: String): List<PhotoDto> =
        withContext(dispatcher) {
            return@withContext httpClient.get(ApiEndpoints.PHOTOS.url) {
                url {
                    parameters.append("page", page.toString())
                    parameters.append("per_page", "20")
                    parameters.append("order_by", orderBy)
                    parameters.append("client_id", CLIENT_ID)
                }
            }.body()
        }

    override suspend fun getRandomPhotos(): List<PhotoDto> = withContext(dispatcher) {
        return@withContext httpClient.get(ApiEndpoints.PHOTOS_RANDOM.url) {
            url {
                parameters.append("count", "30")
                parameters.append("client_id", CLIENT_ID)
            }
        }.body()
    }

    override suspend fun getPhoto(id: String): PhotoDto = withContext(dispatcher) {
        return@withContext httpClient.get(ApiEndpoints.PHOTOS.url) {
            url {
                appendPathSegments(id)
                parameters.append("client_id", CLIENT_ID)
            }
        }.body()
    }

    override suspend fun searchPhoto(query: String): SearchPhotosResultDto =
        withContext(dispatcher) {
            return@withContext httpClient.get(ApiEndpoints.PHOTOS_SEARCH.url) {
                url {
                    parameters.append("query", query)
                    parameters.append("client_id", CLIENT_ID)
                }
            }.body()
        }

    override fun downloadPhoto(photoUrl: String) = channelFlow {
        httpClient.get(photoUrl) {
            url {
                parameters.append("client_id", CLIENT_ID)
            }
            onDownload { bytesSentTotal, contentLength ->
                val progressPercentage = (bytesSentTotal * 100 / contentLength).toInt()
                if (progressPercentage < 100) {
                    send(Pair(null, progressPercentage))
                }
            }
        }
            .body<ByteArray>()
            .run {
                send(Pair(this, 100))
            }
    }
}