package com.velshletter.atlasmonitor.domain.repository

import com.velshletter.atlasmonitor.domain.models.MonitoringData
import com.velshletter.atlasmonitor.domain.models.TripInfo

interface SharedPrefRepository {
    fun saveMonitoringData(data: MonitoringData)
    fun saveTripInfo(tripInfo: TripInfo)
    fun getMonitorData(): MonitoringData
    fun getTripInfo(): TripInfo
}