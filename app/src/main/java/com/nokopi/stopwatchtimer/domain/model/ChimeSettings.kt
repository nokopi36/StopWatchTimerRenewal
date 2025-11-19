package com.nokopi.stopwatchtimer.domain.model

data class ChimeSettings(
    val firstBellSeconds: Int? = null,  // 予鈴時間（秒）
    val secondBellSeconds: Int? = null, // 終鈴時間（秒）
    val endChimeSeconds: Int? = null    // 終了時間（秒）
)