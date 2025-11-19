package com.nokopi.stopwatchtimer.presentation.settings

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.nokopi.stopwatchtimer.R
import com.nokopi.stopwatchtimer.ui.theme.ThemePreview

@Composable
fun TimePickerDialog(
    title: String,
    initialSeconds: Int,
    onDismiss: () -> Unit,
    onConfirm: (hours: Int, minutes: Int, seconds: Int) -> Unit
) {
    val initialHours = initialSeconds / 3600
    val initialMinutes = (initialSeconds % 3600) / 60
    val initialSecs = initialSeconds % 60

    var hourTens by remember { mutableIntStateOf(initialHours / 10) }
    var hourOnes by remember { mutableIntStateOf(initialHours % 10) }
    var minuteTens by remember { mutableIntStateOf(initialMinutes / 10) }
    var minuteOnes by remember { mutableIntStateOf(initialMinutes % 10) }
    var secondTens by remember { mutableIntStateOf(initialSecs / 10) }
    var secondOnes by remember { mutableIntStateOf(initialSecs % 10) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 時の10の位
                    NumberPickerView(
                        value = hourTens,
                        minValue = 0,
                        maxValue = 9,
                        onValueChange = { hourTens = it }
                    )
                    // 時の1の位
                    NumberPickerView(
                        value = hourOnes,
                        minValue = 0,
                        maxValue = 9,
                        onValueChange = { hourOnes = it }
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    // 分の10の位
                    NumberPickerView(
                        value = minuteTens,
                        minValue = 0,
                        maxValue = 5,
                        onValueChange = { minuteTens = it }
                    )
                    // 分の1の位
                    NumberPickerView(
                        value = minuteOnes,
                        minValue = 0,
                        maxValue = 9,
                        onValueChange = { minuteOnes = it }
                    )
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    // 秒の10の位
                    NumberPickerView(
                        value = secondTens,
                        minValue = 0,
                        maxValue = 5,
                        onValueChange = { secondTens = it }
                    )
                    // 秒の1の位
                    NumberPickerView(
                        value = secondOnes,
                        minValue = 0,
                        maxValue = 9,
                        onValueChange = { secondOnes = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val hours = hourTens * 10 + hourOnes
                    val minutes = minuteTens * 10 + minuteOnes
                    val seconds = secondTens * 10 + secondOnes
                    onConfirm(hours, minutes, seconds)
                }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun NumberPickerView(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    // ComposeテーマからテキストカラーとdividerColorを取得
    val textColor = MaterialTheme.colorScheme.onSurface
    val dividerColor = MaterialTheme.colorScheme.outlineVariant

    AndroidView(
        modifier = Modifier
            .width(30.dp)
            .height(120.dp),
        factory = { context ->
            NumberPicker(context).apply {
                setMinValue(minValue)
                setMaxValue(maxValue)
                this.value = value
                setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
                // NumberPickerの表示範囲を制限
                descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            }
        },
        update = { picker ->
            picker.value = value
            picker.textColor = textColor.toArgb()
        }
    )
}

@ThemePreview
@Composable
fun TimePickerDialogPreview() {
    MaterialTheme {
        TimePickerDialog(
            title = "予鈴時間設定",
            initialSeconds = 3665, // 01:01:05
            onDismiss = {},
            onConfirm = { _, _, _ -> }
        )
    }
}
