@file:OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)

package com.unsplash.android.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.unsplash.android.R
import com.unsplash.android.components.ErrorAlertDialog
import com.unsplash.android.components.ProgressBar
import com.unsplash.android.utils.LogCompositions
import com.unsplash.shared.data.remote.generateFakePhoto
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.PhotoDownloadState
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.State
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.Event
import com.unsplash.shared.presentation.photos.details.PhotoDetailsContract.Effect
import com.unsplash.shared.presentation.photos.details.PhotoDetailsViewModel
import com.unsplash.shared.presentation.photos.details.isDownloaded
import com.unsplash.shared.presentation.photos.details.isDownloading
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.random.Random

@Composable
fun PhotoDetailsScreen(
    photoId: String,
    onLogout: () -> Unit
) {
    LogCompositions("PhotoDetailsScreen")

    val viewModel = getViewModel<PhotoDetailsViewModel>()

    LaunchedEffect(photoId) {
        viewModel.sendViewEvent(Event.GetDetailedPhotoAction(photoId))
    }

    val uiState by viewModel.viewState.collectAsStateWithLifecycle()

    viewModel.viewEffect.collectAsStateWithLifecycle(null).value?.let {
        LaunchedEffect(Unit) {
            when (it) {
                is Effect.ShowLoginScreen -> onLogout()
            }
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            PhotoDetailsContent(
                uiState,
                onRefresh = { viewModel.sendViewEvent(Event.RefreshPhotoAction) },
                onDownloadPhotoClick = { viewModel.sendViewEvent(Event.DownloadPhotoAction) },
                onImageLoadError = { viewModel.sendViewEvent(Event.PhotoLoadErrorAction(it)) }
            )
            val responseError = uiState.responseError
            if (responseError != null) {
                ErrorAlertDialog(
                    errorMessage = responseError.message ?: "",
                    onRetryClick = { viewModel.sendViewEvent(Event.RetryFetchPhotoAction) }
                )
            }

            val downloadState = uiState.photoDownloadState
            if (downloadState is PhotoDownloadState.Error) {
                ErrorAlertDialog(
                    errorMessage = downloadState.exception?.message ?: "",
                    onRetryClick = { viewModel.sendViewEvent(Event.DownloadPhotoAction) }
                )
            } else if (uiState.isDownloading() || uiState.isDownloaded()) {
                LaunchedEffect(Random.nextInt()) {
                   val message =
                       if (uiState.isDownloading()) { "The photo is being downloaded..." }
                       else { "The photo has been downloaded."}
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        val result = snackbarHostState
                            .showSnackbar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        when (result) {
                            SnackbarResult.Dismissed -> { viewModel.sendViewEvent(Event.ErrorShackBarDismissAction) }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoDetailsContent(
    uiState: State,
    onRefresh: () -> Unit,
    onDownloadPhotoClick: () -> Unit,
    onImageLoadError: (Throwable) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefresh, onRefresh)
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        val photo = uiState.photo
        when {
            uiState.isLoading -> ProgressBar()
            photo != null -> {
                val scope = rememberCoroutineScope()
                val bottomState =
                    rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

                val latitude = photo.location?.latitude
                val longitude = photo.location?.longitude
                val hasCoordinates =
                    latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0

                ModalBottomSheetLayout(
                    modifier = Modifier.fillMaxWidth(),
                    sheetState = bottomState,
                    sheetContent = {
                        if (hasCoordinates) {
                            MapScreen(
                                name = photo.location?.name,
                                country = photo.location?.country,
                                city = photo.location?.city,
                                latitude = latitude ?: 0.0,
                                longitude = longitude ?: 0.0
                            )
                        }
                    }
                ) {
                    Column {
                        TopBar(
                            photo = photo,
                            isDownloadingPhoto = uiState.isDownloading(),
                            onDownloadPhotoClick = onDownloadPhotoClick
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            AsyncImage(photo, onImageLoadError)
                        }
                        BottomBar(
                            hasCoordinates,
                            onMapIconClick = { scope.launch { bottomState.show() } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AsyncImage(
    photo: Photo,
    onImageLoadError: (Throwable) -> Unit
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.urls.regular)
            .placeholderMemoryCacheKey(photo.id)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        onState = {
            if (it is AsyncImagePainter.State.Error) {
                onImageLoadError(it.result.throwable)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun BottomBar(
    hasCoordinates: Boolean,
    onMapIconClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        if (hasCoordinates) {
            Icon(
                modifier = Modifier
                    .size(42.dp, 482.dp)
                    .clickable {
                        onMapIconClick()
                    },
                painter = painterResource(id = R.drawable.ic_map),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TopBar(
    photo: Photo,
    isDownloadingPhoto: Boolean,
    onDownloadPhotoClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            photo.tagsPreview?.forEach {
                ElevatedSuggestionChip(
                    onClick = { },
                    label = { Text(it.title) }
                )
            }
        }
        Text(text = photo.altDescription ?: photo.description ?: "")
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = photo.downloads.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_download),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            if (!isDownloadingPhoto) {
                Icon(
                    modifier = Modifier.clickable { onDownloadPhotoClick() },
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = photo.views.toString(),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun PhotoDetailsScreenPreview() {
    Surface {
        val uiState = State(
            photo = generateFakePhoto()
        )
        PhotoDetailsContent(
            uiState = uiState,
            onRefresh = {},
            onDownloadPhotoClick = {},
            onImageLoadError = {}
        )
    }
}
