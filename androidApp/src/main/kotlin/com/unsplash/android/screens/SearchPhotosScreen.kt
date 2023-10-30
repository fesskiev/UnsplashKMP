package com.unsplash.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unsplash.android.components.ErrorAlertDialog
import com.unsplash.android.components.ProgressBar
import com.unsplash.android.components.SearchBar
import com.unsplash.android.theme.AppTheme
import com.unsplash.android.utils.LogCompositions
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.State
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.Effect
import com.unsplash.shared.presentation.photos.search.PhotosSearchContract.Event
import com.unsplash.shared.presentation.photos.search.PhotosSearchViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchPhotosScreen(
    isExpandedScreen: Boolean,
    onPhotoDetailsClick: (String) -> Unit,
    onLogout: () -> Unit
) {

    LogCompositions("SearchPhotosScreen")

    val viewModel = getViewModel<PhotosSearchViewModel>()
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()
    viewModel.viewEffect.collectAsStateWithLifecycle(null).value?.let {
        LaunchedEffect(Unit) {
            when (it) {
                is Effect.ShowPhotoDetailsScreen -> onPhotoDetailsClick(it.photoId)
                is Effect.ShowLoginScreen -> onLogout()
            }
        }
    }
    SearchPhotosSurface(
        uiState,
        isExpandedScreen,
        onQueryChange = { viewModel.sendViewEvent(Event.QueryChangeAction(it)) },
        onPhotoClick = { viewModel.sendViewEvent(Event.PhotoClickAction(it.id)) }
    )
    val error = uiState.responseError
    if (error != null) {
        ErrorAlertDialog(
            errorMessage = error.message ?: "",
            onRetryClick = { viewModel.sendViewEvent(Event.RetrySearchPhotosAction) }
        )
    }
}

@Composable
private fun SearchPhotosSurface(
    uiState: State,
    isExpandedScreen: Boolean,
    onQueryChange: (String) -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    val containerColor = MaterialTheme.colorScheme.background
    Surface(
        color = containerColor,
        contentColor = contentColorFor(containerColor)
    ) {
        Box(Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                ProgressBar()
            }
            SearchBar(
                modifier = Modifier.align(Alignment.TopCenter),
                suggestions = uiState.filteredSuggestions,
                query = uiState.query,
                onQueryChange = onQueryChange
            )
            PhotosGrid(
                pagingState = uiState.paging,
                columnsCount = if (isExpandedScreen) 3 else 2,
                photoHeight = if (isExpandedScreen) 300.dp else 180.dp,
                photos = uiState.searchResult?.photos ?: listOf(),
                onLoadMore = { },
                onPhotoClick = onPhotoClick
            )
        }
    }
}

@Preview
@Composable
fun SearchPhotoScreenPreview() {
    AppTheme {
        val uiState = State()
        SearchPhotosSurface(
            uiState = uiState,
            isExpandedScreen = true,
            onQueryChange = { },
            onPhotoClick = { }
        )
    }
}

