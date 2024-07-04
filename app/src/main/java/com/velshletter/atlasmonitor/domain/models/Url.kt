package com.velshletter.atlasmonitor.domain.models

class Url(
    from: String,
    to: String,
    date: String,
) {
    private val urlFstP: String = "https://atlasbus.by/Маршруты/"
    private val urlSecP = "?date="
    private var url: String = urlFstP + from + "/" + to + urlSecP + date

    fun get(): String {
        return url
    }
}