package core.network.photos

import core.network.photos.model.PhotoResponse
import core.network.photos.model.SearchPhotosResultResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosNetworkSourceImpl(private val httpClient: HttpClient) : PhotosNetworkSource {

    private val baseUrl = "https://api.unsplash.com"

    override suspend fun getPhotos(page: Int, orderBy: String): List<PhotoResponse> = withContext(Dispatchers.IO) {
        return@withContext httpClient.get("$baseUrl/photos") {
            url {
                parameters.append("page", page.toString())
                parameters.append("per_page", "20")
                parameters.append("order_by", orderBy)
                parameters.append("client_id", "E3kc6WuwugyPTZNzbzveghBdI8f6zNDG-3Mqq3PTPUo")
            }
        }.body()
    }

    override suspend fun getRandomPhotos(): List<PhotoResponse> = withContext(Dispatchers.IO) {
        return@withContext httpClient.get("$baseUrl/photos/random") {
            url {
                parameters.append("count", "30")
                parameters.append("client_id", "E3kc6WuwugyPTZNzbzveghBdI8f6zNDG-3Mqq3PTPUo")
            }
        }.body()
    }

    override suspend fun getPhoto(id: String): PhotoResponse = withContext(Dispatchers.IO) {
        return@withContext httpClient.get("$baseUrl/photos") {
            url {
                appendPathSegments(id)
                parameters.append("client_id", "E3kc6WuwugyPTZNzbzveghBdI8f6zNDG-3Mqq3PTPUo")
            }
        }.body()
    }

    override suspend fun searchPhoto(query: String): SearchPhotosResultResponse = withContext(Dispatchers.IO) {
        return@withContext httpClient.get("$baseUrl/search/photos") {
            url {
                parameters.append("query", query)
                parameters.append("client_id", "E3kc6WuwugyPTZNzbzveghBdI8f6zNDG-3Mqq3PTPUo")
            }
        }.body()
    }
}