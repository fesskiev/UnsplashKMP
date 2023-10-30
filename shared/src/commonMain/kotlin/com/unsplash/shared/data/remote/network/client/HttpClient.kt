package com.unsplash.shared.data.remote.network.client

import io.ktor.client.HttpClient

expect class HttpClient() {
    val httpClient: HttpClient
}