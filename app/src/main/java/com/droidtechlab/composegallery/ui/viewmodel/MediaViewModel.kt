package com.droidtechlab.composegallery.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidtechlab.composegallery.common.Constants.ALL_IMAGES
import com.droidtechlab.composegallery.common.Constants.ALL_VIDEOS
import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.domain.repository.MediaRepository
import com.droidtechlab.composegallery.ui.media.MediaState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MediaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MediaRepository
) : ViewModel() {

    val type: String = savedStateHandle["request_type"] ?: ""
    val albumId: Long = savedStateHandle["album_id"] ?: -1L
    val albumLabel: String = savedStateHandle["album_label"] ?: ""

    init {
        getMedia()
    }

    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState = _mediaState.asStateFlow()
    private var title: String = ""
    private fun getMedia() = viewModelScope.launch(Dispatchers.IO) {
        val dataSource = if (type == ALL_IMAGES) {
            title = "All Images"
            repository.getAllImages()
        } else if(type == ALL_VIDEOS) {
            title = "All Videos"
            repository.getAllVideos()
        } else {
            title = albumLabel
            repository.getMediaForAlbumId(albumId)
        }

        dataSource.collectLatest {
            val data = it.data ?: emptyList()
            val error = if (it is Result.Error) it.message ?: "An error occurred" else ""
            if (data == mediaState.value.media) return@collectLatest
            _mediaState.emit(MediaState(media = data, error = error, title = title))
        }
    }


}