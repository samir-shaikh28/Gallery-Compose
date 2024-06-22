package com.droidtechlab.composegallery.di

import com.droidtechlab.composegallery.data.repository.MediaRepositoryImpl
import com.droidtechlab.composegallery.domain.repository.MediaRepository
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
    abstract fun bindMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository
}