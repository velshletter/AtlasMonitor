package com.example.domain.models

class UrlConverter(
    tripInfo: TripInfo
) {
    private val urlFstP: String = "https://atlasbus.by/Маршруты/"
    private val urlSecP = "?date="
    private var url: String = urlFstP + tripInfo.from + "/" + tripInfo.to + urlSecP + tripInfo.date

    fun getUrl(): String {
        return url
    }
}