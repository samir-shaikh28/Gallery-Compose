package com.droidtechlab.composegallery.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidtechlab.composegallery.common.Constants.ALL_IMAGES
import com.droidtechlab.composegallery.common.Constants.ALL_VIDEOS
import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.core.pager.DefaultPaginator
import com.droidtechlab.composegallery.domain.model.Media
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

    private val type: String = savedStateHandle["request_type"] ?: ""
    private val albumId: Long = savedStateHandle["album_id"] ?: -1L
    private val albumLabel: String = savedStateHandle["album_label"] ?: ""
    private val albumItemCount: Long = savedStateHandle["album_item_count"] ?: -1L

    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState = _mediaState.asStateFlow()

    private var title: String = ""

    private val paginator = DefaultPaginator(
        initialKey = _mediaState.value.page,

        onLoadUpdated = {
            _mediaState.value = _mediaState.value.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            _mediaState.value = _mediaState.value.copy(isLoading = true)
            val dataSource = when (type) {
                ALL_IMAGES -> {
                    title = "All Images"
                    repository.getAllImages(nextPage)
                }
                ALL_VIDEOS -> {
                    title = "All Videos"
                    repository.getAllVideos(nextPage)
                }
                else -> {
                    title = albumLabel
                    repository.getMediaForAlbumId(albumId, nextPage)
                }
            }
            dataSource
        },
        getNextKey = {
            mediaState.value.page + 1
        },
        onError = {
            _mediaState.value = _mediaState.value.copy(error = it?.localizedMessage.toString(), isLoading = false)
        },
        onSuccess = { items: List<Media>, newKey ->
            _mediaState.value = _mediaState.value.copy(
                media =  _mediaState.value.media + items,
                page = newKey,
                isEndReached = items.isEmpty(),
                title = title,
                itemCount = albumItemCount
            )
        }
    )

    init {
        fetchMedia()
    }

    fun fetchMedia() = viewModelScope.launch(Dispatchers.IO) {
        paginator.loadNextItems()
    }



}