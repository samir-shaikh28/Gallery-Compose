package com.droidtechlab.composegallery.domain.repository

import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.domain.model.Album
import com.droidtechlab.composegallery.domain.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun getAlbums(): Flow<Result<List<Album>>>

    suspend fun getAllImages(): Flow<Result<List<Media>>>

    suspend fun getAllVideos(): Flow<Result<List<Media>>>

    suspend fun getMediaForAlbumId(albumId: Long):  Flow<Result<List<Media>>>
}