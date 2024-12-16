package me.fidelep.ravennews.domain.interfaces

import me.fidelep.ravennews.domain.models.NewsStoryWrapper

interface INewsRepository {
    suspend fun getNews(topic: String): NewsStoryWrapper

    suspend fun deleteNews(deletedNews: Set<String>): Boolean

    suspend fun getDeletedNews(): List<Int>
}
