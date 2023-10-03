package com.example.atlasmonitor

import android.content.Context

sealed class ConnectionState{
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}
