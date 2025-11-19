package com.nokopi.stopwatchtimer.presentation.timer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nokopi.stopwatchtimer.R
import java.util.Locale

@Composable
fun ChimeTimeDisplay(
    label: String,
    seconds: Int?,
    modifier: Modifier = Modifier,
) {
    val timeText = if (seconds != null) {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
    } else {
        stringResource(R.string.time_not_set)
    }

    Row(
        modifier = modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = timeText, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun ChimeTimeDisplayPreview() {
    MaterialTheme {
        Column {
            ChimeTimeDisplay(
                label = "予鈴時間：",
                seconds = 300 // 00:05:00
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChimeTimeDisplay(
                label = "終鈴時間：",
                seconds = 3665 // 01:01:05
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChimeTimeDisplay(
                label = "終了時間：",
                seconds = null // 未設定
            )
        }
    }
}