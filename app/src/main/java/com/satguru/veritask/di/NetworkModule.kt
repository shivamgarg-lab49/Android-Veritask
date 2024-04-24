package com.satguru.veritask.di

import com.satguru.veritask.BuildConfig
import com.satguru.veritask.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Named("baseUrl")
    @Provides
    @Singleton
    fun baseUrl(): String {
        return "https://veritask.vercel.app/"
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("baseUrl") baseUrl: String): ApiService {

        var client = OkHttpClient.Builder().apply {
            this
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(25, TimeUnit.SECONDS)
        }

        if (BuildConfig.DEBUG) {
            client = client.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}