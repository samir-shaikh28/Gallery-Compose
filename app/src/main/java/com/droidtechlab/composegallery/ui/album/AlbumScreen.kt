package com.droidtechlab.composegallery.ui.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.droidtechlab.composegallery.ui.viewmodel.MainViewModel

@Composable
fun AlbumScreen(
    viewModel: MainViewModel
) {
    val state by viewModel.albumsState.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = state.albums,
            key = { item -> item.toString() }
        ) { album ->
            AlbumComponent(album = album) {

            }

        }

    }
}