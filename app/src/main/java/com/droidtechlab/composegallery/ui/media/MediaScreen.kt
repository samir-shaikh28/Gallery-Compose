package com.droidtechlab.composegallery.ui.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.droidtechlab.composegallery.common.Screen
import com.droidtechlab.composegallery.ui.viewmodel.MediaViewModel

@Composable
fun MediaScreen(
    viewModel: MediaViewModel,
    navController: NavHostController,
) {

    val state by viewModel.mediaState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item(span = { GridItemSpan(3) }) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(
                    text = state.title, style = TextStyle(fontSize = 36.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if(state.itemCount != -1L) {
                    Text(
                        text = "${state.itemCount} items", style = TextStyle(fontSize = 12.sp)
                    )
                }

            }

        }

        itemsIndexed(state.media, key = { _, item -> item.id }) { index, media ->
            if (index >= state.media.size - 1 && !state.isLoading && !state.isEndReached) {
                viewModel.fetchMedia()
            }
            MediaComponent(media = media) {
                if (media.isVideo) {
                    navController.navigate("${Screen.VideoPlayer.value}?videoUri=${media.uri}")
                } else {
                    navController.navigate("${Screen.ImagePreview.value}?imageUri=${media.uri}")

                }

            }
        }
    }
}