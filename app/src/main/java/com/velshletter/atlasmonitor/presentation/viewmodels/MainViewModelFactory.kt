package com.velshletter.atlasmonitor.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.velshletter.atlasmonitor.domain.repository.SharedPrefRepository
import com.velshletter.atlasmonitor.domain.usecase.StartMonitorUseCase

class MainViewModelFactory(
    private val startMonitorUseCase: StartMonitorUseCase,
    private val sharedPrefRepositoryImpl: SharedPrefRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(startMonitorUseCase, sharedPrefRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}