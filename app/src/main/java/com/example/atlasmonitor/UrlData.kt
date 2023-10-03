package com.example.atlasmonitor

class UrlData(){
    private val urlFstP: String = "https://atlasbus.by/Маршруты/"
    private val urlSecP = "?date=2023-"
    private var url :String = ""

    fun setUrl(
        from: String,
        to:String,
        date:String
    ){
        url = urlFstP +from +"/"+to+urlSecP+date
    }
    fun getUrl(): String {
        return url
    }
}
