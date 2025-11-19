package com.nokopi.stopwatchtimer.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nokopi.stopwatchtimer.domain.model.ChimeSettings
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "chime_settings")

@Singleton
class ChimeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ChimeRepository {

    private object PreferencesKeys {
        val FIRST_BELL = intPreferencesKey("first_bell_seconds")
        val SECOND_BELL = intPreferencesKey("second_bell_seconds")
        val END_CHIME = intPreferencesKey("end_chime_seconds")
    }

    override fun getChimeSettings(): Flow<ChimeSettings> {
        return context.dataStore.data.map { preferences ->
            ChimeSettings(
                firstBellSeconds = preferences[PreferencesKeys.FIRST_BELL],
                secondBellSeconds = preferences[PreferencesKeys.SECOND_BELL],
                endChimeSeconds = preferences[PreferencesKeys.END_CHIME]
            )
        }
    }

    override suspend fun saveChimeSettings(settings: ChimeSettings) {
        context.dataStore.edit { preferences ->
            settings.firstBellSeconds?.let {
                preferences[PreferencesKeys.FIRST_BELL] = it
            } ?: preferences.remove(PreferencesKeys.FIRST_BELL)

            settings.secondBellSeconds?.let {
                preferences[PreferencesKeys.SECOND_BELL] = it
            } ?: preferences.remove(PreferencesKeys.SECOND_BELL)

            settings.endChimeSeconds?.let {
                preferences[PreferencesKeys.END_CHIME] = it
            } ?: preferences.remove(PreferencesKeys.END_CHIME)
        }
    }

    override suspend fun updateFirstBell(seconds: Int?) {
        context.dataStore.edit { preferences ->
            if (seconds != null) {
                preferences[PreferencesKeys.FIRST_BELL] = seconds
            } else {
                preferences.remove(PreferencesKeys.FIRST_BELL)
            }
        }
    }

    override suspend fun updateSecondBell(seconds: Int?) {
        context.dataStore.edit { preferences ->
            if (seconds != null) {
                preferences[PreferencesKeys.SECOND_BELL] = seconds
            } else {
                preferences.remove(PreferencesKeys.SECOND_BELL)
            }
        }
    }

    override suspend fun updateEndChime(seconds: Int?) {
        context.dataStore.edit { preferences ->
            if (seconds != null) {
                preferences[PreferencesKeys.END_CHIME] = seconds
            } else {
                preferences.remove(PreferencesKeys.END_CHIME)
            }
        }
    }
}
