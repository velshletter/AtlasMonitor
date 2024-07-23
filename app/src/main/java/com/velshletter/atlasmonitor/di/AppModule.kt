package com.velshletter.atlasmonitor.di

import android.content.Context
import com.example.domain.repository.NotificationSender
import com.example.domain.repository.ServiceManager
import com.velshletter.atlasmonitor.notification.AndroidNotificationSender
import com.velshletter.atlasmonitor.service.AndroidServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAndroidNotificationSender(@ApplicationContext context: Context): NotificationSender {
        return AndroidNotificationSender(context = context)
    }

    @Provides
    @Singleton
    fun provideAndroidServiceManager(@ApplicationContext context: Context): ServiceManager {
        return AndroidServiceManager(context = context)
    }
}