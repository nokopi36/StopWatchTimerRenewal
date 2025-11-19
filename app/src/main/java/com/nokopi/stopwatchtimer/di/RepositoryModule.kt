package com.nokopi.stopwatchtimer.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nokopi.stopwatchtimer.data.repository.ChimePresetRepositoryImpl
import com.nokopi.stopwatchtimer.data.repository.ChimeRepositoryImpl
import com.nokopi.stopwatchtimer.domain.repository.ChimePresetRepository
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChimeRepository(
        chimeRepositoryImpl: ChimeRepositoryImpl
    ): ChimeRepository

    @Binds
    @Singleton
    abstract fun bindChimePresetRepository(
        chimePresetRepositoryImpl: ChimePresetRepositoryImpl
    ): ChimePresetRepository

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return GsonBuilder().create()
        }
    }
}
