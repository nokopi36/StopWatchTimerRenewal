package com.nokopi.stopwatchtimer.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.nokopi.stopwatchtimer.R
import com.nokopi.stopwatchtimer.domain.audio.AudioPlayer
import com.nokopi.stopwatchtimer.domain.model.ChimeType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun playChime(chimeType: ChimeType) {
        // 既存の再生を停止してリソースを解放
        mediaPlayer?.release()

        val resourceId = when (chimeType) {
            ChimeType.FIRST_BELL, ChimeType.SECOND_BELL -> R.raw.bell
            ChimeType.END_CHIME -> R.raw.end_chime
        }

        try {
            mediaPlayer = MediaPlayer.create(context, resourceId).apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setOnCompletionListener { mp ->
                    mp.release()
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // 音声ファイルが見つからない場合などのエラーハンドリング
        }
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}