package com.nokopi.stopwatchtimer.presentation.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nokopi.stopwatchtimer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val chimeSettings by viewModel.chimeSettings.collectAsState()
    var showFirstBellDialog by remember { mutableStateOf(false) }
    var showSecondBellDialog by remember { mutableStateOf(false) }
    var showEndChimeDialog by remember { mutableStateOf(false) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }


    SettingScreenContent(
        chimeSettings = chimeSettings,
        onNavigateBack = onNavigateBack,
        onFirstBellClick = { showFirstBellDialog = true },
        onSecondBellClick = { showSecondBellDialog = true },
        onEndChimeClick = { showEndChimeDialog = true },
        onResetClick = { showResetConfirmDialog = true }
    )


    if (showFirstBellDialog) {
        TimePickerDialog(
            title = stringResource(R.string.first_bell_setting),
            initialSeconds = chimeSettings.firstBellSeconds ?: 0,
            onDismiss = { showFirstBellDialog = false },
            onConfirm = { hours, minutes, seconds ->
                viewModel.updateFirstBell(hours, minutes, seconds)
                showFirstBellDialog = false
            }
        )
    }

    if (showSecondBellDialog) {
        TimePickerDialog(
            title = stringResource(R.string.second_bell_setting),
            initialSeconds = chimeSettings.secondBellSeconds ?: 0,
            onDismiss = { showSecondBellDialog = false },
            onConfirm = { hours, minutes, seconds ->
                viewModel.updateSecondBell(hours, minutes, seconds)
                showSecondBellDialog = false
            }
        )
    }

    if (showEndChimeDialog) {
        TimePickerDialog(
            title = stringResource(R.string.end_time_setting),
            initialSeconds = chimeSettings.endChimeSeconds ?: 0,
            onDismiss = { showEndChimeDialog = false },
            onConfirm = { hours, minutes, seconds ->
                viewModel.updateEndChime(hours, minutes, seconds)
                showEndChimeDialog = false
            }
        )
    }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text(stringResource(R.string.reset_confirmation_title)) },
            text = { Text(stringResource(R.string.reset_confirmation_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllSettings()
                        showResetConfirmDialog = false
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetConfirmDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
