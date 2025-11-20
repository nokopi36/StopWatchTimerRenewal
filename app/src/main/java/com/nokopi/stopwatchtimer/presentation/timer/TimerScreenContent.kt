package com.nokopi.stopwatchtimer.presentation.timer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nokopi.stopwatchtimer.R
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.domain.model.TimerState
import com.nokopi.stopwatchtimer.presentation.timer.component.ChimeTimeDisplay
import com.nokopi.stopwatchtimer.ui.theme.ThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreenContent(
    uiState: TimerUiState,
    onNavigateToSettings: () -> Unit,
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onManualChimeClicked: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.timer_title)) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        },
        bottomBar = {
            TimerController(
                isLandscape = isLandscape,
                uiState = uiState,
                onStartClicked = onStartClicked,
                onStopClicked = onStopClicked,
                onResetClicked = onResetClicked,
                onManualChimeClicked = onManualChimeClicked,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp),
            )
        }
    ) { paddingValues ->
        // タイマー表示（画面幅・高さに応じて自動調整）
        BoxWithConstraints(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // 利用可能な幅と高さに基づいてフォントサイズを計算
            // 横幅ベース: "00:00:00" は8文字、概算で幅の85%を使用
            val fontSizeByWidth = (maxWidth.value * 0.85f / 8f * 2.2f).sp
            // 縦幅ベース: テキストの行高は概ねfontSizeの1.2倍なので、高さの80%程度
            val fontSizeByHeight = (maxHeight.value * 0.8f / 1.2f).sp
            // 両方の制約を満たす小さい方を採用
            val fontSize = minOf(fontSizeByWidth.value, fontSizeByHeight.value).sp

            Text(
                text = uiState.formattedTime,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerController(
    isLandscape: Boolean,
    uiState: TimerUiState,
    onStartClicked: () -> Unit,
    onStopClicked: () -> Unit,
    onResetClicked: () -> Unit,
    onManualChimeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(

    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = if (isLandscape) 3 else 1
        ) {
            ChimeTimeDisplay(
                label = stringResource(R.string.first_bell_time),
                seconds = uiState.chimeSettings.firstBellSeconds
            )
            ChimeTimeDisplay(
                label = stringResource(R.string.second_bell_time),
                seconds = uiState.chimeSettings.secondBellSeconds
            )
            ChimeTimeDisplay(
                label = stringResource(R.string.end_time),
                seconds = uiState.chimeSettings.endChimeSeconds
            )
        }
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = if (isLandscape) 4 else 2
        ) {
            Button(
                onClick = onStartClicked,
                enabled = uiState.isStartButtonEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(stringResource(R.string.start))
            }
            Button(
                onClick = onStopClicked,
                enabled = uiState.isStopButtonEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(stringResource(R.string.stop))
            }
            Button(
                onClick = onResetClicked,
                enabled = uiState.isResetButtonEnabled,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(stringResource(R.string.reset))
            }
            IconButton(
                onClick = onManualChimeClicked,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .height(40.dp)
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.manual_chime)
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun TimerScreenContentPreview() {
    MaterialTheme {
        TimerScreenContent(
            uiState = TimerUiState(
                elapsedSeconds = 3665,
                timerState = TimerState.Idle,
                chimeSettings = ChimeSettings(
                    firstBellSeconds = 300,
                    secondBellSeconds = 600,
                    endChimeSeconds = 1200
                ),
                playedChimes = setOf(300)
            ),
            onNavigateToSettings = {},
            onStartClicked = {},
            onStopClicked = {},
            onResetClicked = {},
            onManualChimeClicked = {}
        )
    }
}

@ThemePreview
@Composable
private fun TimerScreenContentLandscapePreview() {
    MaterialTheme {
        TimerScreenContent(
            uiState = TimerUiState(
                elapsedSeconds = 3665,
                timerState = TimerState.Idle,
                chimeSettings = ChimeSettings(
                    firstBellSeconds = 300,
                    secondBellSeconds = 600,
                    endChimeSeconds = 1200
                ),
                playedChimes = setOf(300)
            ),
            onNavigateToSettings = {},
            onStartClicked = {},
            onStopClicked = {},
            onResetClicked = {},
            onManualChimeClicked = {}
        )
    }
}