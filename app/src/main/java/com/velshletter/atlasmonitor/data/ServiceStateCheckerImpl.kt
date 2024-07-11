package com.velshletter.atlasmonitor.data

import android.content.Context
import com.velshletter.atlasmonitor.domain.repository.ServiceStateChecker

class ServiceStateCheckerImpl(private val context: Context) : ServiceStateChecker {
    private val PREFS_NAME = "AtlasMonitor"
    private val PREFS_SERVICE_STATE = "service_running"
    override fun isServiceRunning(): Boolean {
        val prefs = context.getSharedPreferences("AtlasMonitor", Context.MODE_PRIVATE)
        return prefs.getBoolean("service_running", false)
    }

    override fun upDateServiceState(isRunning: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREFS_SERVICE_STATE, isRunning).apply()
    }
}
