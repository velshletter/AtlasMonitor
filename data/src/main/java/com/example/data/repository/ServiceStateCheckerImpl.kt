package com.example.data.repository

import android.content.Context
import com.example.data.data.SharedPrefsConstants.PREFS_NAME
import com.example.data.data.SharedPrefsConstants.PREFS_SERVICE_STATE
import com.example.domain.repository.ServiceStateChecker

class ServiceStateCheckerImpl(private val context: Context) : ServiceStateChecker {

    override fun isServiceRunning(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREFS_SERVICE_STATE, false)
    }

    override fun upDateServiceState(isRunning: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREFS_SERVICE_STATE, isRunning).apply()
    }
}
