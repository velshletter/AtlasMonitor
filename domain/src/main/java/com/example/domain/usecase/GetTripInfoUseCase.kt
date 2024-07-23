package com.example.domain.usecase

import android.util.Log
import com.example.domain.repository.WebsiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class GetTripInfoUseCase(private val websiteRepository: WebsiteRepository) {

    suspend fun execute(url: String) : List<String>{
        val timeArray = mutableListOf<String>()
        return withContext(Dispatchers.IO) {
            try {
                val doc = websiteRepository.getWebsite(url)
                Log.d("MyLog", doc.toString())
                val time =
                    doc.select("div.MuiGrid-grid-md-3.MuiGrid-item.MuiGrid-root:nth-of-type(1)")
                val info =
                    doc.select("div.MuiGrid-grid-md-auto.MuiGrid-item.MuiGrid-root:nth-of-type(3)")
                var i = 0
                var j = 1
                while (i < info.size) {
                    val stTime = time[j].allElements
                    timeArray.add(stTime[3].text())
                    i++; j += 2
                }
            } catch (e: IOException) {
                Log.d("MyLog", e.message!!)
            }
            timeArray
        }
    }
}