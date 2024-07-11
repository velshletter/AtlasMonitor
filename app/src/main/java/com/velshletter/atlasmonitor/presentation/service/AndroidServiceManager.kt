package com.velshletter.atlasmonitor.presentation.service

import android.content.Context
import android.content.Intent
import com.example.domain.repository.ServiceManager

class AndroidServiceManager(
    private val context: Context,
) : com.example.domain.repository.ServiceManager {

    override fun startService() {
        Intent(context, ForegroundService::class.java).also {
            context.startService(it)
        }
    }

    override fun stopService() {
        Intent(context, ForegroundService::class.java).also {
            context.stopService(it)
        }
    }


}