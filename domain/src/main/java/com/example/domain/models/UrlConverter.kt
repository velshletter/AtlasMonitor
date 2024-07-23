package com.example.domain.models

class UrlConverter(
    private val tripInfo: TripInfo,
) {
    fun getUrl(): String {
        return "https://atlasbus.by/Маршруты/${tripInfo.from}/${tripInfo.to}?date=${tripInfo.date}"
    }
}