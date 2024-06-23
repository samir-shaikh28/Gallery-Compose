package com.droidtechlab.composegallery.ui.media

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Scale

@Composable
fun ImagePreviewScreen(
    uri: Uri,
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(uri)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .placeholderMemoryCacheKey(uri.toString())
            .scale(Scale.FILL)
            .build(),
        contentScale = ContentScale.FillBounds
    )
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painter,
        contentDescription = "",
    )

}