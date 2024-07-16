package com.velshletter.atlasmonitor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.repository.SharedPrefRepository
import com.example.domain.repository.WebsiteRepository
import com.example.domain.usecase.StartMonitorUseCase

class MainViewModelFactory(
    private val startMonitorUseCase: StartMonitorUseCase,
    private val sharedPrefRepositoryImpl: SharedPrefRepository,
    private val websiteRepository: WebsiteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(startMonitorUseCase, sharedPrefRepositoryImpl, websiteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}