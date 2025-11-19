package com.nokopi.stopwatchtimer.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nokopi.stopwatchtimer.R
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.presentation.settings.component.ChimeSettingItem
import com.nokopi.stopwatchtimer.ui.theme.ThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContent(
    onNavigateBack: () -> Unit,
    chimeSettings: ChimeSettings,
    onFirstBellClick: () -> Unit,
    onSecondBellClick: () -> Unit,
    onEndChimeClick: () -> Unit,
    onResetClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ChimeSettingItem(
                    label = stringResource(R.string.first_bell_time),
                    seconds = chimeSettings.firstBellSeconds,
                    onClick = onFirstBellClick
                )
            }

            item {
                ChimeSettingItem(
                    label = stringResource(R.string.second_bell_time),
                    seconds = chimeSettings.secondBellSeconds,
                    onClick = onSecondBellClick
                )
            }
            item {
                ChimeSettingItem(
                    label = stringResource(R.string.end_time),
                    seconds = chimeSettings.endChimeSeconds,
                    onClick = onEndChimeClick
                )
            }
            item {
                // リセットボタン
                OutlinedButton(
                    onClick = onResetClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.reset_all_settings))
                }
            }

            item {
                // バージョン情報
                Text(
                    text = stringResource(R.string.version),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun SettingScreenContentPreview() {
    MaterialTheme {
        SettingScreenContent(
            onNavigateBack = {},
            chimeSettings = ChimeSettings(
                firstBellSeconds = 300,
                secondBellSeconds = 600,
                endChimeSeconds = null
            ),
            onFirstBellClick = {},
            onSecondBellClick = {},
            onEndChimeClick = {},
            onResetClick = {}
        )
    }
}