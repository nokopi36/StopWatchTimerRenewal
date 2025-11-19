package com.nokopi.stopwatchtimer.domain.model

sealed class TimerState {
    object Idle : TimerState()       // 初期状態/リセット後
    object Running : TimerState()    // タイマー動作中
    object Stopped : TimerState()    // 一時停止中
}