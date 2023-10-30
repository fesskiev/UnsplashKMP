package com.unsplash.shared.presentation.photos.search

import com.unsplash.shared.data.utils.onLogout
import com.unsplash.shared.data.utils.onResponseFailure
import com.unsplash.shared.data.utils.onSuccess
import com.unsplash.shared.domain.model.SearchResult
import com.unsplash.shared.domain.usecases.SearchPhotosUseCase
import com.unsplash.shared.presentation.viewmodel.BaseViewModel
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.Effect
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.Event
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PhotosSearchViewModel(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : BaseViewModel<Event, State, Effect>()  {

    private var searchJob: Job? = null

    override val initialState: State = State()

    override fun handleViewEvents(viewEvent: Event) {
        when (viewEvent) {
            is Event.QueryChangeAction -> searchPhotos(viewEvent.query)
            is Event.PhotoClickAction -> openPhotoDetailsScreen(viewEvent.photoId)
            is Event.RetrySearchPhotosAction -> retrySearchPhotos()
        }
    }

    private fun searchPhotos(query: String) {
        showQueryValue(query)
        when {
            query.isEmpty() -> {
                showClearSearchPhotos()
                return
            }
            query.length >= 3 -> {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(1000)
                    showProgress()
                    searchPhotosUseCase(query)
                        .onSuccess { showSearchPhotos(query, it) }
                        .onLogout { logout() }
                        .onResponseFailure{ showError(it) }
                }
            }
        }
    }

    private fun retrySearchPhotos() {
        val query = viewState.value.query
        searchPhotos(query)
    }

    private fun openPhotoDetailsScreen(photoId: String) {
        setViewEffect {
            Effect.ShowPhotoDetailsScreen(photoId)
        }
    }

    private fun showProgress() {
        setViewState {
            copy(
                isLoading = true,
                responseError = null
            )
        }
    }

    private fun showQueryValue(query: String) {
        setViewState {
            copy(
                query = query,
                filteredSuggestions = matchQueryWithSuggestions(query)
            )
        }
    }

    private fun showClearSearchPhotos() {
        setViewState {
            copy(searchResult = null)
        }
    }

    private fun showSearchPhotos(query: String, searchResult: SearchResult) {
        setViewState {
            copy(
                isLoading = false,
                suggestions = addSuggestion(query),
                responseError = null,
                searchResult = searchResult
            )
        }
    }

   private fun State.matchQueryWithSuggestions(query: String): List<String> {
       return suggestions.filter {
           it.startsWith(query)
       }
   }

    private fun State.addSuggestion(query: String): ArrayDeque<String> {
        if (suggestions.contains(query)) {
            return suggestions
        }
        suggestions.addFirst(query)
        return suggestions
    }

    private fun showError(responseError: Throwable) {
        setViewState {
            copy(
                isLoading = false,
                responseError = responseError
            )
        }
    }

    private fun logout() {
        setViewEffect {
            Effect.ShowLoginScreen
        }
    }
}