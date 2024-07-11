package com.velshletter.atlasmonitor.domain.repository

import android.content.Context

interface NotificationSender {
    fun sendNotification(foundedTime: String)
}