package com.example.domain.repository

interface ServiceStateChecker {
    fun isServiceRunning(): Boolean
    fun upDateServiceState(isRunning: Boolean)
}