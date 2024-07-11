package com.example.data.data

import com.example.domain.repository.WebsiteRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebsiteRepositoryImpl : WebsiteRepository {

    override fun getWebsite(url: String): Document {
        return Jsoup.connect(url).get()
    }

}