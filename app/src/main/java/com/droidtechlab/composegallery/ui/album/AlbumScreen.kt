package com.droidtechlab.composegallery.ui.album

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.droidtechlab.composegallery.common.Constants
import com.droidtechlab.composegallery.common.Request
import com.droidtechlab.composegallery.common.Screen
import com.droidtechlab.composegallery.ui.component.Loader
import com.droidtechlab.composegallery.ui.viewmodel.AlbumViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel,
    navController: NavHostController
) {
    val permissionsToRequest = listOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
    )

    var isAllPermissionGranted by remember {
        mutableStateOf(false)
    }


    var toggleFlag by remember {
        mutableStateOf(false)
    }

    val state by viewModel.albumsState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val multiplePermissionResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { perms ->
                var allPermGranted = false
                permissionsToRequest.forEach { permission ->
                    if (!isPermissionGranted(context, permission)) {
                        toggleFlag = !toggleFlag
                        return@rememberLauncherForActivityResult
                    }
                    allPermGranted = true
                }
                isAllPermissionGranted = allPermGranted
            }
        )




    LaunchedEffect(key1 = isAllPermissionGranted, key2 = toggleFlag) {
        permissionsToRequest.forEach {
            if (!isPermissionGranted(context, it)) {
                showSnackBar(scope, snackbarHostState, multiplePermissionResultLauncher, permissionsToRequest)
                return@LaunchedEffect
            }
            isAllPermissionGranted = true
        }
        if (isAllPermissionGranted && state.albums.isEmpty()) {
            viewModel.getAlbums()
        }

    }


    Scaffold(modifier = Modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        if (!isAllPermissionGranted) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Permission Required!",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else if (state.isLoading) {
            Loader(modifier = Modifier)
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(paddingValues)
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
    }
}

fun showSnackBar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    multiplePermissionResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String,
            Boolean>>,
    permissionsToRequest: List<String>
) {
    scope.launch {
        val result = snackbarHostState.showSnackbar(
            message = "Allow Permission",
            actionLabel = "Allow",
            duration = SnackbarDuration.Indefinite
        )
        when (result) {
            SnackbarResult.Dismissed -> {
            }

            SnackbarResult.ActionPerformed -> {
                multiplePermissionResultLauncher.launch(permissionsToRequest.toTypedArray())
            }
        }

    }
}


private fun isPermissionGranted(context: Context, name: String) = ContextCompat.checkSelfPermission(
    context, name
) == PackageManager.PERMISSION_GRANTED