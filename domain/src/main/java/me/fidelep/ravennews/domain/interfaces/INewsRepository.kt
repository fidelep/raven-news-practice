package me.fidelep.ravennews.domain.interfaces

import me.fidelep.ravennews.domain.models.NewsArticleWrapper

interface INewsRepository {
    suspend fun getNews(topic: String): NewsArticleWrapper
}
