package com.unsplash.shared.domain.usecases

import com.unsplash.shared.data.remote.PhotosNetworkSource
import com.unsplash.shared.data.utils.getResult
import com.unsplash.shared.data.utils.Result
import com.unsplash.shared.domain.model.SearchResult
import com.unsplash.shared.domain.toSearchResult

class SearchPhotosUseCase(private val networkSource: PhotosNetworkSource) {

    suspend operator fun invoke(query: String): Result<SearchResult> = getResult {
        validateArguments(query)
        networkSource.searchPhoto(query).toSearchResult()
    }

    private fun validateArguments(query: String) {
        if (query.isEmpty() || query.isBlank()) {
            throw IllegalArgumentException("query must not be empty or blank")
        }
    }
}