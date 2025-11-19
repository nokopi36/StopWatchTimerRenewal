package com.nokopi.stopwatchtimer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokopi.stopwatchtimer.domain.model.ChimePreset
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.domain.repository.ChimePresetRepository
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

enum class PresetError {
    EMPTY_NAME,
    DUPLICATE_NAME
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val chimeRepository: ChimeRepository,
    private val presetRepository: ChimePresetRepository
) : ViewModel() {

    private val _chimeSettings = MutableStateFlow(ChimeSettings())
    val chimeSettings: StateFlow<ChimeSettings> = _chimeSettings.asStateFlow()

    private val _presets = MutableStateFlow<List<ChimePreset>>(emptyList())
    val presets: StateFlow<List<ChimePreset>> = _presets.asStateFlow()

    private val _presetNameInput = MutableStateFlow("")
    val presetNameInput: StateFlow<String> = _presetNameInput.asStateFlow()

    private val _errorMessage = MutableStateFlow<PresetError?>(null)
    val errorMessage: StateFlow<PresetError?> = _errorMessage.asStateFlow()

    private val _showDeleteConfirmDialog = MutableStateFlow<String?>(null)
    val showDeleteConfirmDialog: StateFlow<String?> = _showDeleteConfirmDialog.asStateFlow()

    init {
        loadSettings()
        loadPresets()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            chimeRepository.getChimeSettings().collect { settings ->
                _chimeSettings.value = settings
            }
        }
    }

    private fun loadPresets() {
        viewModelScope.launch {
            presetRepository.getAllPresets().collect { presetList ->
                _presets.value = presetList
            }
        }
    }

    fun updateFirstBell(hours: Int, minutes: Int, seconds: Int) {
        viewModelScope.launch {
            val totalSeconds = hours * 3600 + minutes * 60 + seconds
            chimeRepository.updateFirstBell(totalSeconds)
        }
    }

    fun updateSecondBell(hours: Int, minutes: Int, seconds: Int) {
        viewModelScope.launch {
            val totalSeconds = hours * 3600 + minutes * 60 + seconds
            chimeRepository.updateSecondBell(totalSeconds)
        }
    }

    fun updateEndChime(hours: Int, minutes: Int, seconds: Int) {
        viewModelScope.launch {
            val totalSeconds = hours * 3600 + minutes * 60 + seconds
            chimeRepository.updateEndChime(totalSeconds)
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            chimeRepository.updateFirstBell(null)
            chimeRepository.updateSecondBell(null)
            chimeRepository.updateEndChime(null)
        }
    }

    fun updatePresetNameInput(name: String) {
        _presetNameInput.value = name
        _errorMessage.value = null
    }

    fun savePreset() {
        val name = _presetNameInput.value.trim()
        if (name.isEmpty()) {
            _errorMessage.value = PresetError.EMPTY_NAME
            return
        }

        viewModelScope.launch {
            val currentPresets = _presets.value
            if (currentPresets.any { it.name == name }) {
                _errorMessage.value = PresetError.DUPLICATE_NAME
                return@launch
            }

            val preset = ChimePreset(
                id = UUID.randomUUID().toString(),
                name = name,
                settings = _chimeSettings.value
            )

            presetRepository.savePreset(preset)
            _presetNameInput.value = ""
            _errorMessage.value = null
        }
    }

    fun applyPreset(presetId: String) {
        viewModelScope.launch {
            val preset = _presets.value.find { it.id == presetId }
            if (preset != null) {
                chimeRepository.saveChimeSettings(preset.settings)
            }
        }
    }

    fun requestDeletePreset(presetId: String) {
        _showDeleteConfirmDialog.value = presetId
    }

    fun cancelDeletePreset() {
        _showDeleteConfirmDialog.value = null
    }

    fun confirmDeletePreset(presetId: String) {
        viewModelScope.launch {
            presetRepository.deletePreset(presetId)
            _showDeleteConfirmDialog.value = null
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
