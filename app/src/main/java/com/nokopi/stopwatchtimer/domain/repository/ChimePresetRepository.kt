package com.nokopi.stopwatchtimer.domain.repository

import com.nokopi.stopwatchtimer.domain.model.ChimePreset
import kotlinx.coroutines.flow.Flow

interface ChimePresetRepository {
    fun getAllPresets(): Flow<List<ChimePreset>>
    suspend fun savePreset(preset: ChimePreset)
    suspend fun deletePreset(presetId: String)
}

