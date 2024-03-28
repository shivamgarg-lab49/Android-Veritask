package com.satguru.veritask.di

import android.app.Application
import com.satguru.veritask.services.ApiService
import com.satguru.veritask.utils.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return SharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideRepositoryService(
        apiService: ApiService,
        sharedPreferences: SharedPreferences
    ): RepositoryService {
        return RepositoryServiceImpl(apiService, sharedPreferences)
    }
}