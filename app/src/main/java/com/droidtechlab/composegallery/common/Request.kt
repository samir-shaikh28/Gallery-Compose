package com.droidtechlab.composegallery.common

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Request(uri: Uri?) {
    data object AllImages : Request(null)
    data object AllVideos : Request(null)
    data class Album(val uri: Uri?) : Request(uri)
}