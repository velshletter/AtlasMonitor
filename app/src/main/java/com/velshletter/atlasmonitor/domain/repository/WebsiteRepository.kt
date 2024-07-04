package com.velshletter.atlasmonitor.domain.repository

import org.jsoup.nodes.Document

interface WebsiteRepository {

    fun getWebsite(url: String): Document

}