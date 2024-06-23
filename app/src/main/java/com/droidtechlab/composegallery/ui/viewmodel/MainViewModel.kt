package com.droidtechlab.composegallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.domain.repository.MediaRepository
import com.droidtechlab.composegallery.ui.album.AlbumState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MediaRepository
): ViewModel() {

    private val _albumsState = MutableStateFlow(AlbumState())
    val albumsState = _albumsState.asStateFlow()

    init {
        getAlbums()
    }


    private fun getAlbums()  = viewModelScope.launch(Dispatchers.IO) {
        _albumsState.emit(AlbumState(isLoading = true))
        repository.getAlbums().collectLatest {
            val data = it.data ?: emptyList()
            val error =
                if (it is Result.Error) it.message ?: "An error occurred" else ""
            if (data == albumsState.value.albums) return@collectLatest
            _albumsState.emit(AlbumState(albums = data, error = error, isLoading = false))
        }
    }
}