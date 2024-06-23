package com.droidtechlab.composegallery.ui.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.droidtechlab.composegallery.common.Constants
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
            Text(modifier = Modifier.padding(16.dp),
                text = state.title, style = TextStyle(fontSize = 36.sp))
        }
        items(state.media, key = { item -> item.toString() }) { media ->
            MediaComponent(media = media) {
                if(media.isVideo) {
                    navController.navigate("${Screen.VideoPlayer.value}?videoUri=${media.uri}")
                }

            }
        }
    }
}