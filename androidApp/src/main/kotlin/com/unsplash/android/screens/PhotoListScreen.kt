@file:OptIn(ExperimentalMaterialApi::class)

package com.unsplash.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unsplash.android.components.PagingProgressBar
import com.unsplash.android.components.PhotoListTopBar
import com.unsplash.android.components.ProgressBar
import com.unsplash.android.utils.LogCompositions
import com.unsplash.android.utils.isScrolledToTheEnd
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.unsplash.android.R
import com.unsplash.android.components.ErrorAlertDialog
import com.unsplash.shared.data.remote.generateFakePhoto
import com.unsplash.shared.domain.model.OrderType
import com.unsplash.shared.presentation.common.PagingState
import com.unsplash.shared.domain.model.Photo
import com.unsplash.shared.presentation.photos.list.PhotoListContract.State
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Event
import com.unsplash.shared.presentation.photos.list.PhotoListViewModel
import com.unsplash.shared.presentation.photos.list.PhotoListContract.Effect
import org.koin.androidx.compose.getViewModel

@Composable
fun PhotoListScreen(
    modifier: Modifier,
    isExpandedScreen: Boolean,
    photoDetailsScreen: @Composable (photoId: String) -> Unit = { },
    onSearchClick: () -> Unit,
    onPhotoDetailsClick: (String) -> Unit,
    onOpenDrawerClick: () -> Unit,
    onLogout: () -> Unit
) {
    LogCompositions("PhotoListScreen")

    val viewModel = getViewModel<PhotoListViewModel>()
    val uiState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.sendViewEvent(Event.GetFirstPhotosPageAction)
    }
    viewModel.viewEffect.collectAsStateWithLifecycle(null).value?.let {
        LaunchedEffect(Unit) {
            when (it) {
                is Effect.ShowPhotoDetailsScreen -> onPhotoDetailsClick(it.photoId)
                is Effect.ShowLoginScreen -> onLogout()
            }
        }
    }

    PhotoListScaffold(
        modifier,
        isExpandedScreen,
        uiState,
        onOrderClick = { viewModel.sendViewEvent(Event.OrderPhotosAction(it)) },
        onRefresh = { viewModel.sendViewEvent(Event.RefreshPhotosAction) },
        onLoadMore = { viewModel.sendViewEvent(Event.LoadMorePhotosAction) },
        onPhotoClick = { viewModel.sendViewEvent(Event.PhotoClickAction(it.id)) },
        onSearchClick,
        onOpenDrawerClick,
        photoDetailsScreen
    )
    val error = uiState.responseError
    if (error != null) {
        ErrorAlertDialog(
            errorMessage = error.message ?: "",
            onRetryClick = { viewModel.sendViewEvent(Event.RetryFetchPhotosAction) }
        )
    }
}

@Composable
private fun PhotoListScaffold(
    modifier: Modifier,
    isExpandedScreen: Boolean,
    uiState: State,
    onOrderClick: (OrderType) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
    onSearchClick: () -> Unit,
    onOpenDrawerClick: () -> Unit,
    photoDetailsScreen: @Composable (photoId: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PhotoListTopBar(
                isExpandedScreen = isExpandedScreen,
                selectedOrderType = uiState.orderType,
                onOrderClick = onOrderClick,
                onSearchClick = onSearchClick,
                onOpenDrawerClick = onOpenDrawerClick
            )
        }
    ) {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            if (isExpandedScreen) {
                var photoId by rememberSaveable { mutableStateOf("") }
                Row {
                    Box(modifier = Modifier.weight(1f)) {
                        if (uiState.isLoading) {
                            ProgressBar()
                        }
                        PullRefreshPhotosGrid(
                            pagingState = uiState.paging,
                            isRefresh = uiState.isRefresh,
                            photos = uiState.photos,
                            columnsCount = 3,
                            photoHeight = 300.dp,
                            onRefresh = onRefresh,
                            onLoadMore = onLoadMore,
                            onPhotoClick = { photo -> photoId = photo.id }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (photoId.isNotEmpty()) {
                            photoDetailsScreen(photoId)
                        }
                    }
                }
            } else {
                if (uiState.isLoading) {
                    ProgressBar()
                }
                PullRefreshPhotosGrid(
                    pagingState = uiState.paging,
                    isRefresh = uiState.isRefresh,
                    photos = uiState.photos,
                    columnsCount = 2,
                    photoHeight = 180.dp,
                    onRefresh = onRefresh,
                    onLoadMore = onLoadMore,
                    onPhotoClick = onPhotoClick
                )
            }
        }
    }
}

@Composable
private fun PullRefreshPhotosGrid(
    pagingState: PagingState,
    isRefresh: Boolean,
    photos: List<Photo>,
    columnsCount: Int,
    photoHeight: Dp,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(isRefresh, onRefresh)
    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        PhotosGrid(
            pagingState = pagingState,
            photoHeight = photoHeight,
            columnsCount = columnsCount,
            photos = photos,
            onLoadMore = onLoadMore,
            onPhotoClick = onPhotoClick
        )
        PullRefreshIndicator(isRefresh, pullRefreshState)
    }
}

@Composable
fun PhotosGrid(
    pagingState: PagingState,
    columnsCount: Int,
    photoHeight: Dp,
    photos: List<Photo>,
    onLoadMore: () -> Unit,
    onPhotoClick: (Photo) -> Unit
) {
    LogCompositions("PhotosList")
    val scrollState = rememberLazyGridState()
    val loadMore by remember {
        derivedStateOf {
            scrollState.isScrolledToTheEnd()
        }
    }
    if (loadMore) {
        LaunchedEffect(Unit) {
            onLoadMore()
        }
    }
    LazyVerticalGrid(
        state = scrollState,
        columns = GridCells.Fixed(columnsCount),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(photos) { item ->
            PhotoCell(item, photoHeight, onPhotoClick)
        }
        when {
            pagingState.error != null -> item { RetryButton(onRetryClick = onLoadMore) }
            pagingState.loadMore -> item { PagingProgressBar() }
        }
    }
}

@Composable
fun PhotoCell(
    item: Photo,
    photoHeight: Dp,
    onPhotoClick: (Photo) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(item.urls.small)
            .size(Size.ORIGINAL)
            .memoryCacheKey(item.id)
            .build()
    )
    Box {
        if (painter.state is AsyncImagePainter.State.Error) {
            PhotoItemError()
        } else {
            PhotoItem(
                item = item,
                photoHeight = photoHeight,
                placeholderVisible = painter.state is AsyncImagePainter.State.Loading,
                painter = painter,
                onPhotoClick = onPhotoClick
            )
        }
    }
}

@Composable
fun PhotoItem(
    item: Photo,
    photoHeight: Dp,
    placeholderVisible: Boolean,
    painter: AsyncImagePainter,
    onPhotoClick: (Photo) -> Unit
) {
    Column {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(photoHeight)
                .clip(RoundedCornerShape(12.dp))
                .placeholder(
                    visible = placeholderVisible,
                    color = Color.Gray,
                    highlight = PlaceholderHighlight.shimmer()
                )
                .clickable { onPhotoClick(item) },
            contentScale = ContentScale.Crop
        )
        if (photoHeight > 200.dp) {
            ItemBottomFullContent(item)
        } else {
            ItemBottomShortContent(item)
        }
    }
}

@Composable
private fun ItemBottomShortContent(photo: Photo) {
    Row(
        modifier = Modifier
            .height(32.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = photo.user.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ItemBottomFullContent(photo: Photo) {
    Row(
        modifier = Modifier
            .height(42.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_time),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = photo.createdDate,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Text(
            text = photo.user.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = photo.likes.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = null
            )
        }
    }
}

@Composable
fun RetryButton(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = onRetryClick) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun PhotoItemError() {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            text = "Loading Error!"
        )
    }
}

@Preview
@Composable
fun PhotoItemBottomFullContentPreview() {
    Surface {
        ItemBottomFullContent(generateFakePhoto())
    }
}