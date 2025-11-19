package com.nokopi.stopwatchtimer.presentation.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nokopi.stopwatchtimer.domain.audio.AudioPlayer
import com.nokopi.stopwatchtimer.domain.model.ChimeType
import com.nokopi.stopwatchtimer.domain.model.TimerState
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val chimeRepository: ChimeRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadChimeSettings()
    }

    private fun loadChimeSettings() {
        viewModelScope.launch {
            chimeRepository.getChimeSettings().collect { settings ->
                _uiState.update { it.copy(chimeSettings = settings) }
            }
        }
    }

    fun onStartClicked() {
        _uiState.update { it.copy(timerState = TimerState.Running) }
        startTimer()
    }

    fun onStopClicked() {
        _uiState.update { it.copy(timerState = TimerState.Stopped) }
        stopTimer()
    }

    fun onResetClicked() {
        stopTimer()
        _uiState.update {
            it.copy(
                timerState = TimerState.Idle,
                elapsedSeconds = 0,
                playedChimes = emptySet()
            )
        }
    }

    fun onManualChimeClicked() {
        audioPlayer.playChime(ChimeType.FIRST_BELL)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _uiState.update { state ->
                    val newElapsed = state.elapsedSeconds + 1
                    checkAndPlayChime(newElapsed, state)
                    state.copy(elapsedSeconds = newElapsed)
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun checkAndPlayChime(currentSeconds: Int, state: TimerUiState) {
        val settings = state.chimeSettings

        // 既に再生したチャイムはスキップ
        if (currentSeconds in state.playedChimes) {
            return
        }

        when (currentSeconds) {
            settings.firstBellSeconds -> {
                audioPlayer.playChime(ChimeType.FIRST_BELL)
                _uiState.update { it.copy(playedChimes = it.playedChimes + currentSeconds) }
            }
            settings.secondBellSeconds -> {
                audioPlayer.playChime(ChimeType.SECOND_BELL)
                _uiState.update { it.copy(playedChimes = it.playedChimes + currentSeconds) }
            }
            settings.endChimeSeconds -> {
                audioPlayer.playChime(ChimeType.END_CHIME)
                _uiState.update { it.copy(playedChimes = it.playedChimes + currentSeconds) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        audioPlayer.release()
    }
}
