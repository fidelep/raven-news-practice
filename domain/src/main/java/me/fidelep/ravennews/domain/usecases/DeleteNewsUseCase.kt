package me.fidelep.ravennews.domain.usecases

import me.fidelep.ravennews.domain.interfaces.INewsRepository

class DeleteNewsUseCase(
    private val repository: INewsRepository,
) : IUseCase<Int, Boolean> {
    override suspend fun invoke(param: Int): Boolean {
        val deletedNews = repository.getDeletedNews().toMutableList()

        deletedNews.add(param)
        val insertionResult = repository.deleteNews(deletedNews.map { it.toString() }.toSet())

        return insertionResult
    }
}
