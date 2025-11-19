package com.nokopi.stopwatchtimer.presentation.settings.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nokopi.stopwatchtimer.R
import java.util.Locale

@Composable
fun ChimeSettingItem(
    label: String,
    seconds: Int?,
    onClick: () -> Unit
) {
    val timeText = if (seconds != null) {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, secs)
    } else {
        stringResource(R.string.time_not_set)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(
                    Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings_icon),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChimeSettingItemPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ChimeSettingItem(
                label = "予鈴時間",
                seconds = 300, // 00:05:00
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChimeSettingItem(
                label = "終鈴時間",
                seconds = 600, // 00:10:00
                onClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
            ChimeSettingItem(
                label = "終了時間",
                seconds = null, // 未設定
                onClick = {}
            )
        }
    }
}