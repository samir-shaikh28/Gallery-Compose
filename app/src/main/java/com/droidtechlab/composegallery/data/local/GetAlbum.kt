package com.droidtechlab.composegallery.data.local

import android.content.ContentResolver
import android.content.ContentUris
import android.database.MergeCursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.droidtechlab.composegallery.domain.model.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ContentResolver.getAlbums(): List<Album> {
    return withContext(Dispatchers.IO) {
        val timeStart = System.currentTimeMillis()
        val albums = ArrayList<Album>()
        val query = Query.AlbumQuery()
        val bundle = query.bundle ?: Bundle()
        val albumQuery = query.copy(
            bundle = bundle.apply {
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.MediaColumns.DATE_MODIFIED)
                )
            },
        )

        query(albumQuery).use {
            with(it) {
                while (moveToNext()) {
                    try {
                        val albumId = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
                        val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        val label: String? = try {
                            getString(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME))
                        } catch (e: Exception) {
                            Build.MODEL
                        }
                        val thumbnailPath =
                            getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                        val thumbnailRelativePath =
                            getString(getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
                        val thumbnailDate =
                            getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
                        val mimeType =
                            getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
                        val contentUri = if (mimeType.contains("image"))
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        else
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        val album = Album(
                            id = albumId,
                            label = label ?: Build.MODEL,
                            uri = ContentUris.withAppendedId(contentUri, id),
                            pathToThumbnail = thumbnailPath,
                            relativePath = thumbnailRelativePath,
                            timestamp = thumbnailDate,
                            count = 1
                        )
                        val currentAlbum = albums.find { albm -> albm.id == albumId }
                        if (currentAlbum == null)
                            albums.add(album)
                        else {
                            val i = albums.indexOf(currentAlbum)
                            albums[i].count++
                            if (albums[i].timestamp <= thumbnailDate) {
                                album.count = albums[i].count
                                albums[i] = album
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return@withContext albums
    }
}