package com.velshletter.atlasmonitor.di

import android.content.Context
import com.example.data.repository.ServiceStateCheckerImpl
import com.example.data.repository.SharedPrefRepositoryImpl
import com.example.data.repository.WebsiteRepositoryImpl
import com.example.domain.repository.ServiceStateChecker
import com.example.domain.repository.SharedPrefRepository
import com.example.domain.repository.WebsiteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideWebsiteRepository() : WebsiteRepository{
        return WebsiteRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideSharedPrefRepository(@ApplicationContext context: Context) : SharedPrefRepository{
        return SharedPrefRepositoryImpl(context = context)
    }

    @Provides
    @Singleton
    fun provideServiceStateChecker(@ApplicationContext context: Context) : ServiceStateChecker{
        return ServiceStateCheckerImpl(context = context)
    }


}