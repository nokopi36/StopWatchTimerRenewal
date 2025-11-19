package com.nokopi.stopwatchtimer.presentation.timer

import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.domain.model.TimerState

data class TimerUiState(
    val elapsedSeconds: Int = 0,
    val timerState: TimerState = TimerState.Idle,
    val chimeSettings: ChimeSettings = ChimeSettings(),
    val playedChimes: Set<Int> = emptySet() // 既に鳴らしたチャイムの秒数を記録
) {
    val formattedTime: String
        get() {
            val hours = elapsedSeconds / 3600
            val minutes = (elapsedSeconds % 3600) / 60
            val seconds = elapsedSeconds % 60
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

    val isStartButtonEnabled: Boolean
        get() = timerState is TimerState.Idle || timerState is TimerState.Stopped

    val isStopButtonEnabled: Boolean
        get() = timerState is TimerState.Running

    val isResetButtonEnabled: Boolean
        get() = timerState is TimerState.Stopped
}
