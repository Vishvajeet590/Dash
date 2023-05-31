package com.vishwajeet.listeddash.di

import com.vishwajeet.listeddash.api.DashboardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.inopenapp.com/")
            .build()
    }

    @Singleton
    @Provides
    fun providesDashboardAPI(retrofit: Retrofit): DashboardApi {
        return retrofit.create(DashboardApi::class.java)
    }

}