package com.droidtechlab.composegallery.domain.model

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Parcelable
import android.webkit.MimeTypeMap
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import coil3.compose.EqualityDelegate
import com.droidtechlab.composegallery.common.Constants
import com.droidtechlab.composegallery.util.getDate
import java.io.File
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.random.Random


@Immutable
@Parcelize
data class Media(
    val id: Long = 0,
    val label: String,
    val uri: Uri,
    val path: String,
    val relativePath: String,
    val albumID: Long,
    val albumLabel: String,
    val timestamp: Long,
    val expiryTimestamp: Long? = null,
    val takenTimestamp: Long? = null,
    val fullDate: String,
    val mimeType: String,
    val duration: String? = null,
) : Parcelable {

    @IgnoredOnParcel
    @Stable
    val isVideo: Boolean = mimeType.startsWith("video/") && duration != null

    @IgnoredOnParcel
    @Stable
    val isImage: Boolean = mimeType.startsWith("image/")
}