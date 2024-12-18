package me.fidelep.ravennews.domain.interfaces

import me.fidelep.ravennews.domain.models.NewsStoryWrapper

interface INewsRepository {
    suspend fun getNews(topic: String? = null): NewsStoryWrapper

    /*TODO: deleteNews and getDeletedNews might be an independent file
       shared in both repositories or  inherited as abstract class*/
    suspend fun deleteNews(deletedNews: Set<String>): Boolean

    suspend fun getDeletedNews(): List<Int>
}
