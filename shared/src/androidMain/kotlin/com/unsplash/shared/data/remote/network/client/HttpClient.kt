package com.unsplash.shared.data.remote.network.client

import com.unsplash.shared.data.remote.network.client.exceptions.AuthException
import com.unsplash.shared.data.remote.network.client.exceptions.ClientException
import com.unsplash.shared.data.remote.network.client.exceptions.NoNetworkException
import com.unsplash.shared.data.remote.network.client.exceptions.ServerException
import com.unsplash.shared.data.remote.network.client.utils.ApiEndpoints
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.net.UnknownHostException

actual class HttpClient {
    actual val httpClient: HttpClient = HttpClient(Android) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = ApiEndpoints.BASE.url
            }
            contentType(ContentType.Application.Json)
        }
        expectSuccess = false
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is UnknownHostException -> throw NoNetworkException()
                }
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                val exceptionResponse = clientException.response
                if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                    throw AuthException()
                }
                val code = exceptionResponse.status.value
                if (code >= 500) {
                    throw ServerException()
                } else {
                    throw ClientException()
                }
            }
        }
    }
}