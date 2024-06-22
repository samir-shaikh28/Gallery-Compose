package com.droidtechlab.composegallery.data.repository

import android.content.Context
import android.provider.MediaStore
import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.core.contentFlowObserver
import com.droidtechlab.composegallery.data.local.Query
import com.droidtechlab.composegallery.data.local.getAlbums
import com.droidtechlab.composegallery.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MediaRepository {

    override suspend fun getVideoAlbums() = context.contentFlowObserver(VIDEO_URI).map {
        try {
            Result.Success(data = context.contentResolver.getAlbums(Query.VideoAlbumQuery()))
        } catch (e: Exception) {
            Result.Error(message = e.localizedMessage ?: "An error occurred")
        }
    }


    override suspend fun getImagesAlbums() = context.contentFlowObserver(IMAGE_URI).map {
        try {
            Result.Success(data = context.contentResolver.getAlbums(Query.PhotoAlbumQuery()))
        } catch (e: Exception) {
            Result.Error(message = e.localizedMessage ?: "An error occurred")
        }
    }


    override suspend fun getAllImages() {
        //
    }

    override suspend fun getAllVideos() {
        //
    }

    companion object {
        private val IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private val VIDEO_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    }
}