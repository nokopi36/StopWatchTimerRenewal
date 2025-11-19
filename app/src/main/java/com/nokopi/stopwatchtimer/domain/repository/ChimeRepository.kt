package com.nokopi.stopwatchtimer.domain.repository

import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import kotlinx.coroutines.flow.Flow

interface ChimeRepository {
    fun getChimeSettings(): Flow<ChimeSettings>
    suspend fun saveChimeSettings(settings: ChimeSettings)
    suspend fun updateFirstBell(seconds: Int?)
    suspend fun updateSecondBell(seconds: Int?)
    suspend fun updateEndChime(seconds: Int?)
}
