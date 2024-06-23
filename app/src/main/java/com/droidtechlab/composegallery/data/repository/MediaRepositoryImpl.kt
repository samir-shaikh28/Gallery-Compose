package com.droidtechlab.composegallery.data.repository

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.droidtechlab.composegallery.core.Result
import com.droidtechlab.composegallery.core.contentFlowObserver
import com.droidtechlab.composegallery.data.local.Query
import com.droidtechlab.composegallery.data.local.getAlbums
import com.droidtechlab.composegallery.data.local.getMedia
import com.droidtechlab.composegallery.domain.model.Media
import com.droidtechlab.composegallery.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MediaRepository {

    override suspend fun getAlbums() = context.contentFlowObserver(URIs).map {
        try {
//            val query = Query.PhotoQuery()
//            val pageSize = if(page < 2) 50 else 80
            Result.Success(data = context.contentResolver.getAlbums())
        } catch (e: Exception) {
            Result.Error(message = e.localizedMessage ?: "An error occurred")
        }
    }

    override suspend fun getAllImages(page: Int) = context.contentFlowObserver(IMAGE_URI).map {

        try {
            val query = Query.PhotoQuery()
            val pageSize = if(page < 2) 50 else 80
            Result.Success(data = context.contentResolver.getMedia(query.updatePage(query, page, pageSize)))
        } catch (e: Exception) {
            Result.Error(message = e.localizedMessage ?: "An error occurred")
        }
    }


    override suspend fun getAllVideos(page: Int) = context.contentFlowObserver(VIDEO_URI).map {
        try {
            val query = Query.VideoQuery()
            val pageSize = if(page < 2) 50 else 120
            Result.Success(data = context.contentResolver.getMedia(query.updatePage(query, page, pageSize)))
        } catch (e: Exception) {
            Result.Error(message = e.localizedMessage ?: "An error occurred")
        }
    }

    override suspend fun getMediaForAlbumId(albumId: Long, page: Int): Flow<Result<List<Media>>> {
        val pageSize = if(page < 2) 50 else 120

        val query = Query.MediaQuery().copy(
            bundle = Bundle().apply {
                putString(
                    ContentResolver.QUERY_ARG_SQL_SELECTION,
                    MediaStore.MediaColumns.BUCKET_ID + "= ?"
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                    arrayOf(albumId.toString())
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.MediaColumns.DATE_MODIFIED)
                )
            }
        )

        return context.contentFlowObserver(URIs).map {
            try {
                Result.Success(data = context.contentResolver.getMedia(query.updatePage(query, page, pageSize)).sortedByDescending { it.timestamp })
            } catch (e: Exception) {
                Result.Error(message = e.localizedMessage ?: "An error occurred")
            }
        }
    }

    companion object {
        private val URIs = arrayOf(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )

        private val IMAGE_URI = arrayOf(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        private val VIDEO_URI = arrayOf(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )


    }
}