package com.droidtechlab.composegallery.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.droidtechlab.composegallery.common.Screen
import com.droidtechlab.composegallery.ui.album.AlbumScreen
import com.droidtechlab.composegallery.ui.media.ImagePreviewScreen
import com.droidtechlab.composegallery.ui.media.MediaScreen
import com.droidtechlab.composegallery.ui.media.VideoPlayerScreen
import com.droidtechlab.composegallery.ui.viewmodel.AlbumViewModel
import com.droidtechlab.composegallery.ui.viewmodel.MediaViewModel

@Composable
fun NavigationComponent() {

    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Screen.AlbumScreen.value
    ) {

        composable(Screen.AlbumScreen.value) {
            val albumViewModel = hiltViewModel<AlbumViewModel>()
            AlbumScreen(viewModel = albumViewModel, navController = navController)
        }

        composable("${Screen.Media.value}?request_type={request_type}&album_label={album_label}&album_id={album_id}&album_item_count={album_item_count}",
            arguments = listOf(
                navArgument(name = "request_type") {
                    type = NavType.StringType
                },
                navArgument(name = "album_label") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(name = "album_id") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(name = "album_item_count") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val mediaViewModel = hiltViewModel<MediaViewModel>()
            MediaScreen(
                viewModel = mediaViewModel,
                navController = navController
            )
        }

        composable("${Screen.VideoPlayer.value}?videoUri={videoUri}",
            arguments = listOf(
                navArgument(name = "videoUri") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val uriStr = backStackEntry.arguments?.getString("videoUri")
            VideoPlayerScreen(uri = Uri.parse(uriStr), navController)
        }

        composable("${Screen.ImagePreview.value}?imageUri={imageUri}",
            arguments = listOf(
                navArgument(name = "imageUri") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val uriStr = backStackEntry.arguments?.getString("imageUri")
            ImagePreviewScreen(uri = Uri.parse(uriStr))
        }
    }
}