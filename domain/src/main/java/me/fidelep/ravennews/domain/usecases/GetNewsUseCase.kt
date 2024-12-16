package me.fidelep.ravennews.domain.usecases

import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsStoryWrapper

class GetNewsUseCase(
    private val repository: INewsRepository,
) : IUseCase<String, NewsStoryWrapper> {
    override suspend fun invoke(param: String): NewsStoryWrapper = repository.getNews(param)
}
