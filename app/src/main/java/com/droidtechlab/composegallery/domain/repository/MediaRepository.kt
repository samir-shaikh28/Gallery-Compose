package com.droidtechlab.composegallery.domain.repository

import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    suspend fun getVideoAlbums(): Flow<Result<List<Album>>>

    suspend fun getImagesAlbums(): Flow<Result<List<Album>>>

    suspend fun getAllImages()

    suspend fun getAllVideos()
}