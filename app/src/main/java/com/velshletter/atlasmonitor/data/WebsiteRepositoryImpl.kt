package com.velshletter.atlasmonitor.data

import com.velshletter.atlasmonitor.domain.repository.WebsiteRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class WebsiteRepositoryImpl : WebsiteRepository{

    override fun getWebsite(url: String): Document {
        return Jsoup.connect(url).get()
    }

}