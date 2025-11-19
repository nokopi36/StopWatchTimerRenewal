package com.nokopi.stopwatchtimer.domain.audio

import com.nokopi.stopwatchtimer.domain.model.ChimeType

interface AudioPlayer {
    fun playChime(chimeType: ChimeType)
    fun release()
}