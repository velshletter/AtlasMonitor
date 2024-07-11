package com.example.domain.repository

import com.example.domain.models.MonitoringData
import com.example.domain.models.TripInfo

interface SharedPrefRepository {
    fun saveMonitoringData(data: MonitoringData)
    fun saveTripInfo(tripInfo: TripInfo)
    fun getMonitorData(): MonitoringData
    fun getTripInfo(): TripInfo
}