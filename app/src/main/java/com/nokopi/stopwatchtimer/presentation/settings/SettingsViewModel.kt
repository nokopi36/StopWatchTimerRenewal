package com.nokopi.stopwatchtimer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val chimeRepository: ChimeRepository
) : ViewModel() {

    private val _chimeSettings = MutableStateFlow(ChimeSettings())
    val chimeSettings: StateFlow<ChimeSettings> = _chimeSettings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            chimeRepository.getChimeSettings().collect { settings ->
                _chimeSettings.value = settings
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
}
