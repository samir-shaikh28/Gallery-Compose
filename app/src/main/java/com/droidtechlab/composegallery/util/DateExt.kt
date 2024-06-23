package com.droidtechlab.composegallery.util

import android.text.format.DateFormat
import com.droidtechlab.composegallery.common.Constants
import java.util.*

fun Long.getDate(
    format: CharSequence = Constants.DEFAULT_DATE_FORMAT,
    ): String {
    val mediaDate = Calendar.getInstance(Locale.US)
    mediaDate.timeInMillis = this * 1000L
    return DateFormat.format(format, mediaDate).toString()
}