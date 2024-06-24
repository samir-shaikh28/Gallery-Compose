package com.droidtechlab.composegallery.ui.album

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidtechlab.composegallery.common.Request

@Composable
fun TopPills(onPillClick: (Request) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Pill(modifier = Modifier.clickable {
            onPillClick(Request.AllImages)
        },"All Images")
        Spacer(modifier = Modifier.width(12.dp))
        Pill(modifier = Modifier.clickable {
            onPillClick(Request.AllVideos)
        }, "All Videos")
    }
}

@Composable
fun Pill(modifier: Modifier = Modifier, title: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(text = title)
        }
    }
}

@Preview
@Composable
fun PreviewTopPills() {
    TopPills() {}
}