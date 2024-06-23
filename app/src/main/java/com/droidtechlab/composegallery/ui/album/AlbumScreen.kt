package com.droidtechlab.composegallery.ui.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.droidtechlab.composegallery.common.Constants
import com.droidtechlab.composegallery.common.Request
import com.droidtechlab.composegallery.common.Screen
import com.droidtechlab.composegallery.ui.component.Loader
import com.droidtechlab.composegallery.ui.viewmodel.MainViewModel

@Composable
fun AlbumScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val state by viewModel.albumsState.collectAsStateWithLifecycle()

    if(state.isLoading) {
        Loader(modifier = Modifier)
    }
    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        item(span = { GridItemSpan(2) }) {
            TopPills() { request ->
                if (request == Request.AllImages) {
                    navController.navigate("${Screen.Media.value}?request_type=${Constants.ALL_IMAGES}")
                } else {
                    navController.navigate("${Screen.Media.value}?request_type=${Constants.ALL_VIDEOS}")
                }
            }
        }

        items(
            items = state.albums,
            key = { item -> item.toString() }
        ) { album ->
            AlbumComponent(album = album) {
                navController.navigate(
                    "${Screen.Media.value}?request_type=${Constants.Album}" +
                            "&album_label=${album.label}" +
                            "&album_id=${album.id}"
                )
            }
        }
    }
}