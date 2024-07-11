package com.velshletter.atlasmonitor.presentation.service

import android.content.Context
import android.content.Intent
import com.velshletter.atlasmonitor.domain.repository.ServiceManager

class AndroidServiceManager(
    private val context: Context,
) : ServiceManager {

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