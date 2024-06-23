package com.droidtechlab.composegallery.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.database.MergeCursor
import android.os.Build
import android.provider.MediaStore
import com.droidtechlab.composegallery.common.Constants
import com.droidtechlab.composegallery.domain.model.Media
import com.droidtechlab.composegallery.util.getDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ContentResolver.query(albumQuery: Query) =  withContext(Dispatchers.IO) {
    MergeCursor(
        arrayOf(
            query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                albumQuery.projection,
                albumQuery.bundle,
                null
            ),
            query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                albumQuery.projection,
                albumQuery.bundle,
                null
            )
        )
    )
}



@Throws(Exception::class)
fun Cursor.getMediaFromCursor(): Media {
    val id: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
    val path: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
    val relativePath: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
    val title: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
    val albumID: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
    val albumLabel: String = try {
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME))
    } catch (_: Exception) {
        Build.MODEL
    }
    val takenTimestamp: Long? = try {
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN))
    } catch (_: Exception) {
        null
    }
    val modifiedTimestamp: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
    val duration: String? = try {
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
    } catch (_: Exception) {
        null
    }
    val mimeType: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))

    val expiryTimestamp: Long? = try {
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_EXPIRES))
    } catch (_: Exception) {
        null
    }
    val contentUri = if (mimeType.contains("image"))
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    else
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    val uri = ContentUris.withAppendedId(contentUri, id)
    val formattedDate = modifiedTimestamp.getDate(Constants.FULL_DATE_FORMAT)
    return Media(
        id = id,
        label = title,
        uri = uri,
        path = path,
        relativePath = relativePath,
        albumID = albumID,
        albumLabel = albumLabel,
        timestamp = modifiedTimestamp,
        takenTimestamp = takenTimestamp,
        expiryTimestamp = expiryTimestamp,
        fullDate = formattedDate,
        duration = duration,
        mimeType = mimeType
    )
}