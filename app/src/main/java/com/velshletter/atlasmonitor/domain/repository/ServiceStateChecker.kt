package com.velshletter.atlasmonitor.domain.repository

interface ServiceStateChecker {
    fun isServiceRunning(): Boolean
    fun upDateServiceState(isRunning: Boolean)
}