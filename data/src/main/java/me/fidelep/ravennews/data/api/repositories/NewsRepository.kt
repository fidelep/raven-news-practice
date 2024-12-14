package me.fidelep.ravennews.data.api.repositories

import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.api.models.toModel
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsArticleWrapper
import retrofit2.HttpException
import java.io.IOException

class NewsRepository(
    private val newsApi: NewsApi,
) : INewsRepository {
    override suspend fun getNews(topic: String): NewsArticleWrapper =
        try {
            NewsArticleWrapper.Success(newsApi.getNews(topic).articles.map { it.toModel() })
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> NewsArticleWrapper.NetworkError
                is HttpException ->
                    NewsArticleWrapper.Error(
                        throwable.code(),
                        throwable.message(),
                    )

                else -> NewsArticleWrapper.GenericError
            }
        }
}
