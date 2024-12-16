package me.fidelep.ravennews.data.repositories

import android.util.Log
import me.fidelep.ravennews.data.db.dao.NewsStoryDao
import me.fidelep.ravennews.data.db.model.NewsStoryEntity
import me.fidelep.ravennews.data.db.model.fromModel
import me.fidelep.ravennews.data.db.model.toModel
import me.fidelep.ravennews.domain.interfaces.ILocalNewsRepository
import me.fidelep.ravennews.domain.interfaces.INewsPreferences
import me.fidelep.ravennews.domain.models.NewsStoryModel
import me.fidelep.ravennews.domain.models.NewsStoryWrapper
import retrofit2.HttpException
import java.io.IOException

class LocalNewsRepository(
    private val newsStoryDao: NewsStoryDao,
    private val newsPreferences: INewsPreferences,
) : ILocalNewsRepository {
    private val tag = LocalNewsRepository::class.simpleName

    override suspend fun getNews(topic: String?): NewsStoryWrapper =
        try {
            NewsStoryWrapper.Success(
                newsStoryDao
                    .getAll()
                    .filter { !it.title.isNullOrEmpty() && !getDeletedNews().contains(it.storyId) }
                    .map { it.toModel() },
            )
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException ->
                    NewsStoryWrapper.Error(
                        0x04,
                        throwable.message ?: "Error reading from DB",
                    )

                is HttpException ->
                    NewsStoryWrapper.Error(
                        throwable.code(),
                        throwable.message(),
                    )

                else -> NewsStoryWrapper.GenericError
            }
        }

    override suspend fun deleteNews(deletedNews: Set<String>): Boolean = newsPreferences.addToDeletedNews(deletedNews)

    override suspend fun getDeletedNews(): List<Int> = newsPreferences.getDeletedNews().map { it.toInt() }

    override suspend fun saveNews(vararg news: NewsStoryModel) {
        try {
            newsStoryDao.insert(*news.map { NewsStoryEntity.fromModel(it) }.toTypedArray())
        } catch (throwable: Throwable) {
            Log.d(tag, "Dao exception -> ${throwable.message}")
        }
    }

    override suspend fun removeNews() {
        newsStoryDao.cleanTable()
    }
}
