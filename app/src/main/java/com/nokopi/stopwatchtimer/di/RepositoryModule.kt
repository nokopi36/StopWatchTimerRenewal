package com.nokopi.stopwatchtimer.di

import com.nokopi.stopwatchtimer.data.repository.ChimeRepositoryImpl
import com.nokopi.stopwatchtimer.domain.repository.ChimeRepository
import dagger.Binds
import dagger.Module
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
}
