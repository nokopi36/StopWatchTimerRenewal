package com.nokopi.stopwatchtimer.di

import com.nokopi.stopwatchtimer.data.audio.AudioPlayerImpl
import com.nokopi.stopwatchtimer.domain.audio.AudioPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AudioModule {

    @Binds
    @Singleton
    abstract fun bindAudioPlayer(
        audioPlayerImpl: AudioPlayerImpl
    ): AudioPlayer
}
