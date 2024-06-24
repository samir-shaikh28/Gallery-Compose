package com.droidtechlab.composegallery.ui.media

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.droidtechlab.composegallery.domain.model.Media
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MediaState(
    val media: List<Media> = emptyList(),
    val isLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val page: Int = 1,
    val itemCount: Long = -1L,
    val title: String = "",
    val error: String = ""
) : Parcelable