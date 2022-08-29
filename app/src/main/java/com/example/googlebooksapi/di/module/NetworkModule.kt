package com.example.googlebooksapi.di.module

import com.example.googlebooksapi.model.remote.BookApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideBookApi() = BookApi()
}