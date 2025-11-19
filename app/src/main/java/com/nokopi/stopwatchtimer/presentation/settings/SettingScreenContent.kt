package com.nokopi.stopwatchtimer.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nokopi.stopwatchtimer.BuildConfig
import com.nokopi.stopwatchtimer.R
import com.nokopi.stopwatchtimer.domain.model.ChimePreset
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.presentation.settings.component.ChimeSettingItem
import com.nokopi.stopwatchtimer.presentation.settings.PresetError
import com.nokopi.stopwatchtimer.ui.theme.ThemePreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenContent(
    onNavigateBack: () -> Unit,
    chimeSettings: ChimeSettings,
    presets: List<ChimePreset>,
    presetNameInput: String,
    errorMessage: PresetError?,
    onFirstBellClick: () -> Unit,
    onSecondBellClick: () -> Unit,
    onEndChimeClick: () -> Unit,
    onResetClick: () -> Unit,
    onPresetNameInputChange: (String) -> Unit,
    onSavePreset: () -> Unit,
    onApplyPreset: (String) -> Unit,
    onDeletePreset: (String) -> Unit,
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

            // プリセットセクション
            item {
                PresetSection(
                    presets = presets,
                    presetNameInput = presetNameInput,
                    errorMessage = errorMessage,
                    onPresetNameInputChange = onPresetNameInputChange,
                    onSavePreset = onSavePreset,
                    onApplyPreset = onApplyPreset,
                    onDeletePreset = onDeletePreset
                )
            }

            item {
                // バージョン情報
                Text(
                    text = stringResource(R.string.version, BuildConfig.VERSION_NAME),
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

@Composable
private fun PresetSection(
    presets: List<ChimePreset>,
    presetNameInput: String,
    errorMessage: PresetError?,
    onPresetNameInputChange: (String) -> Unit,
    onSavePreset: () -> Unit,
    onApplyPreset: (String) -> Unit,
    onDeletePreset: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // セクションヘッダー（折りたたみ可能）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.preset_section_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (presets.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.preset_count, presets.size),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "折りたたむ" else "展開する",
                    modifier = Modifier.size(24.dp)
                )
            }

            if (isExpanded) {
                // プリセット保存フォーム
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = presetNameInput,
                        onValueChange = onPresetNameInputChange,
                        label = { Text(stringResource(R.string.preset_name_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorMessage != null,
                        supportingText = if (errorMessage != null) {
                            {
                                Text(
                                    when (errorMessage) {
                                        PresetError.EMPTY_NAME -> stringResource(R.string.preset_name_empty_error)
                                        PresetError.DUPLICATE_NAME -> stringResource(R.string.preset_name_duplicate_error)
                                        null -> ""
                                    }
                                )
                            }
                        } else null
                    )
                    FilledTonalButton(
                        onClick = onSavePreset,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.save_preset))
                    }
                }

                // プリセット一覧
                if (presets.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    presets.forEach { preset ->
                        PresetItem(
                            preset = preset,
                            onApplyPreset = { onApplyPreset(preset.id) },
                            onDeletePreset = { onDeletePreset(preset.id) }
                        )
                        if (preset != presets.last()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetItem(
    preset: ChimePreset,
    onApplyPreset: () -> Unit,
    onDeletePreset: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onApplyPreset() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = preset.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { onDeletePreset() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_preset),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
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
            presets = listOf(
                ChimePreset(
                    id = "1",
                    name = "会議用",
                    settings = ChimeSettings(300, 600, null)
                ),
                ChimePreset(
                    id = "2",
                    name = "講演用",
                    settings = ChimeSettings(600, 1200, 1800)
                )
            ),
            presetNameInput = "",
            errorMessage = null,
            onFirstBellClick = {},
            onSecondBellClick = {},
            onEndChimeClick = {},
            onResetClick = {},
            onPresetNameInputChange = {},
            onSavePreset = {},
            onApplyPreset = {},
            onDeletePreset = {}
        )
    }
}