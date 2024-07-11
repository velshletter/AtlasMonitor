package com.example.domain.repository

interface NotificationSender {
    fun sendNotification(foundedTime: String)
}