package com.velshletter.atlasmonitor.di

import com.example.domain.repository.NotificationSender
import com.example.domain.repository.ServiceManager
import com.example.domain.repository.ServiceStateChecker
import com.example.domain.repository.WebsiteRepository
import com.example.domain.usecase.GetTripInfoUseCase
import com.example.domain.usecase.StartMonitorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideStartMonitorUseCase(
        websiteRepository: WebsiteRepository,
        notificationSender: NotificationSender,
        androidServiceManager: ServiceManager,
        serviceStateChecker: ServiceStateChecker,
    ): StartMonitorUseCase {
        return StartMonitorUseCase(
            websiteRepository = websiteRepository,
            notificationSender = notificationSender,
            serviceManager = androidServiceManager,
            serviceStateChecker = serviceStateChecker
        )
    }

    @Provides
    fun provideGetTripInfoUseCase(websiteRepository: WebsiteRepository): GetTripInfoUseCase {
        return GetTripInfoUseCase(websiteRepository = websiteRepository)
    }


}