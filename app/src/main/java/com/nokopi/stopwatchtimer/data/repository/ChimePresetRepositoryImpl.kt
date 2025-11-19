package com.nokopi.stopwatchtimer.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nokopi.stopwatchtimer.domain.model.ChimePreset
import com.nokopi.stopwatchtimer.domain.repository.ChimePresetRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.presetDataStore: DataStore<Preferences> by preferencesDataStore(name = "chime_presets")

@Singleton
class ChimePresetRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ChimePresetRepository {

    private object PreferencesKeys {
        val PRESETS = stringPreferencesKey("presets")
    }

    override fun getAllPresets(): Flow<List<ChimePreset>> {
        return context.presetDataStore.data.map { preferences ->
            val json = preferences[PreferencesKeys.PRESETS] ?: "[]"
            try {
                val type = object : TypeToken<List<ChimePreset>>() {}.type
                gson.fromJson<List<ChimePreset>>(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun savePreset(preset: ChimePreset) {
        context.presetDataStore.edit { preferences ->
            val currentPresets = try {
                val json = preferences[PreferencesKeys.PRESETS] ?: "[]"
                val type = object : TypeToken<List<ChimePreset>>() {}.type
                gson.fromJson<List<ChimePreset>>(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }

            val updatedPresets = if (currentPresets.any { it.id == preset.id }) {
                // 既存のプリセットを更新
                currentPresets.map { if (it.id == preset.id) preset else it }
            } else {
                // 新しいプリセットを追加
                currentPresets + preset
            }

            preferences[PreferencesKeys.PRESETS] = gson.toJson(updatedPresets)
        }
    }

    override suspend fun deletePreset(presetId: String) {
        context.presetDataStore.edit { preferences ->
            val currentPresets = try {
                val json = preferences[PreferencesKeys.PRESETS] ?: "[]"
                val type = object : TypeToken<List<ChimePreset>>() {}.type
                gson.fromJson<List<ChimePreset>>(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }

            val updatedPresets = currentPresets.filter { it.id != presetId }
            preferences[PreferencesKeys.PRESETS] = gson.toJson(updatedPresets)
        }
    }
}

