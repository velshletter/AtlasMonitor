package com.example.domain.repository

import org.jsoup.nodes.Document

interface WebsiteRepository {

    fun getWebsite(url: String): Document

}