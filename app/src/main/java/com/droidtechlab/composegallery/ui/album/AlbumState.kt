package com.droidtechlab.composegallery.ui.album

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.droidtechlab.composegallery.domain.model.Album
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class AlbumState(
    val albums: List<Album> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
) : Parcelable