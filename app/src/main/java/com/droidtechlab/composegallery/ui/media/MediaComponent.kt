package com.droidtechlab.composegallery.ui.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.size.Scale
import com.droidtechlab.composegallery.R
import com.droidtechlab.composegallery.domain.model.Media

@Composable
fun MediaComponent(
    media: Media,
    onItemClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
            )
            .aspectRatio(1f)
            .clickable(onClick = onItemClick)
    ) {


        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(media.uri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .placeholderMemoryCacheKey(media.toString())
                .scale(Scale.FIT)
                .build(),
            contentScale = ContentScale.FillBounds
        )
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            contentDescription = media.label,
            contentScale = ContentScale.Crop,
        )
    }
}