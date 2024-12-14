package me.fidelep.ravennews.domain.usecases

import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsArticleWrapper

class GetNewsUseCase(
    private val repository: INewsRepository,
) {
    suspend operator fun invoke(topic: String): NewsArticleWrapper = repository.getNews(topic)
}
