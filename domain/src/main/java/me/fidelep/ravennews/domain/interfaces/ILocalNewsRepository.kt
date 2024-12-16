package me.fidelep.ravennews.domain.interfaces

import me.fidelep.ravennews.domain.models.NewsStoryModel

interface ILocalNewsRepository : INewsRepository {
    suspend fun saveNews(vararg news: NewsStoryModel)

    suspend fun removeNews()
}
