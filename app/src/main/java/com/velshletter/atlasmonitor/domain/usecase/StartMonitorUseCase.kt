package com.velshletter.atlasmonitor.domain.usecase

import android.util.Log
import com.velshletter.atlasmonitor.domain.models.TimeItem
import com.velshletter.atlasmonitor.domain.repository.NotificationSender
import com.velshletter.atlasmonitor.domain.repository.ServiceManager
import com.velshletter.atlasmonitor.domain.repository.ServiceStateChecker
import com.velshletter.atlasmonitor.domain.repository.WebsiteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StartMonitorUseCase(
    private val websiteRepository: WebsiteRepository,
    private val notificationSender: NotificationSender,
    private val serviceManager: ServiceManager,
    private val serviceStateChecker: ServiceStateChecker,
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun execute(url: String, timeList: List<TimeItem>) {
        serviceManager.startService()
        Log.d("MyLog", "started")
        coroutineScope.launch {
            while (true) {
                try {
                    val doc = websiteRepository.getWebsite(url)
                    val info =
                        doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")
                    Log.d("MyLog", info.text())
                    for (i in timeList.indices) {
                        if (timeList[i].isSelected) {
                            val sits = info[i].allElements
                            if (sits.size > 1) {
                                notificationSender.sendNotification(timeList[i].time)
                                serviceManager.stopService()
                                Log.d("MyLog", "found & stopped")
                                return@launch
                            }
                        }
                    }
                    if(!serviceStateChecker.isServiceRunning()){
                        Log.d("MyLog", "stopped")
                        coroutineScope.cancel()
                        break
                    }
                } catch (e: Exception) {
                    Log.d("MyLog", e.message.toString())
                    continue
                } finally {
                    kotlinx.coroutines.delay(5000)
                }
            }
        }
    }
}
