package com.example.googlebooksapi.di.module

import com.example.googlebooksapi.model.Repository
import com.example.googlebooksapi.model.RepositoryImpl
import com.example.googlebooksapi.model.remote.BookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    fun provideRepository(network: BookApi): Repository =
        RepositoryImpl(network)
}