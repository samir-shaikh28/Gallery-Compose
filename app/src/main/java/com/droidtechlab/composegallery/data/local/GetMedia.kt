package com.droidtechlab.composegallery.data.local

import android.content.ContentResolver
import com.droidtechlab.composegallery.domain.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun ContentResolver.getMedia(
    mediaQuery: Query,
): List<Media> {
    return withContext(Dispatchers.IO) {
        val timeStart = System.currentTimeMillis()
        val media = ArrayList<Media>()
        query(mediaQuery).use { cursor ->
            while (cursor.moveToNext()) {
                try {
                    media.add(cursor.getMediaFromCursor())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return@withContext media
    }
}