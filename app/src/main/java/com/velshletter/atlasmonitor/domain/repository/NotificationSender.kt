package com.velshletter.atlasmonitor.domain.repository

interface NotificationSender {
    fun sendNotification(foundedTime: String)
}