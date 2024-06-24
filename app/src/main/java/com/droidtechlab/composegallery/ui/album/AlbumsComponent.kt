package com.droidtechlab.composegallery.ui.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.allowRgb565
import coil3.size.Scale
import com.droidtechlab.composegallery.domain.model.Album

@Composable
fun AlbumComponent(
    modifier: Modifier = Modifier,
    album: Album,
    onAlbumClick: () -> Unit
) {

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
        ) {
            AlbumImage(
                album = album,
                onAlbumClick = onAlbumClick,
            )
        }
        // Title
        Text(
            text = album.label,
            style = TextStyle(
                fontSize = 14.sp,
            ),
            maxLines = 1
        )
        // Count
        Text(
            text = "${album.count} items",
            style = TextStyle(
                fontSize = 10.sp
            ),
            maxLines = 1
        )
    }
}

@Composable
fun AlbumImage(
    album: Album,
    onAlbumClick: () -> Unit
) {
    if (album.count == 0L) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .alpha(0.8f)
                .clip(RoundedCornerShape(16.dp))
                .padding(48.dp)
                .clickable(onClick = onAlbumClick)
        )
    } else {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(album.uri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .placeholderMemoryCacheKey(album.id.toString())
                .scale(Scale.FIT)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentScale = ContentScale.FillBounds,
        )
        Image(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onAlbumClick),
            painter = painter,
            contentDescription = album.label,
            contentScale = ContentScale.Crop,
        )
    }
}