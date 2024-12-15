package me.fidelep.ravennews.data.api.repositories

import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.api.models.toModel
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsStoryWrapper
import retrofit2.HttpException
import java.io.IOException

class NewsRepository(
    private val newsApi: NewsApi,
) : INewsRepository {
    override suspend fun getNews(topic: String): NewsStoryWrapper =
        try {
            NewsStoryWrapper.Success(newsApi.getNews(topic).stories.map { it.toModel() })
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> NewsStoryWrapper.NetworkError
                is HttpException ->
                    NewsStoryWrapper.Error(
                        throwable.code(),
                        throwable.message(),
                    )

                else -> NewsStoryWrapper.GenericError
            }
        }
}
