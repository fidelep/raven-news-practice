package me.fidelep.ravennews.domain.interfaces

import me.fidelep.ravennews.domain.models.NewsStoryWrapper

interface INewsRepository {
    suspend fun getNews(topic: String): NewsStoryWrapper
}
