package com.velshletter.atlasmonitor.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.velshletter.atlasmonitor.domain.models.MonitoringData
import com.velshletter.atlasmonitor.domain.models.TripInfo
import com.velshletter.atlasmonitor.domain.repository.SharedPrefRepository

class SharedPrefRepositoryImpl(context: Context) : SharedPrefRepository {

    private val PREFS_NAME = "AtlasMonitor"
    private val PREFS_URL = "url"
    private val PREFS_TIMELIST = "time_list"
    private val PREFS_FROM = "from"
    private val PREFS_TO = "to"

    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveMonitoringData(data: MonitoringData) {
        val jsonList = Gson().toJson(data.timeList)
        prefs.edit().putString(PREFS_URL, data.url).apply()
        prefs.edit().putString(PREFS_TIMELIST, jsonList).apply()
    }

    override fun saveTripInfo(tripInfo: TripInfo) {
        prefs.edit().putString(PREFS_FROM, tripInfo.from).apply()
        prefs.edit().putString(PREFS_TO, tripInfo.to).apply()
    }

    override fun getMonitorData(): MonitoringData {
        val url = prefs.getString(PREFS_URL, "") ?: ""
        val timeListJson = prefs.getString(PREFS_TIMELIST, "") ?: ""
        val type = object : TypeToken<List<String>>() {}.type
        val timeList: List<String> = Gson().fromJson(timeListJson, type)
        return MonitoringData(url, timeList)
    }

    override fun getTripInfo(): TripInfo {
        val from = prefs.getString(PREFS_FROM, "") ?: ""
        val to = prefs.getString(PREFS_TO, "") ?: ""
        return TripInfo(from, to, "")
    }
}