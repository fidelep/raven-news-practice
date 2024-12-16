package me.fidelep.ravennews.domain.usecases

import android.util.Log
import me.fidelep.ravennews.domain.interfaces.ILocalNewsRepository
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsStoryWrapper

class GetNewsUseCase(
    private val remote: INewsRepository,
    private val local: ILocalNewsRepository,
) : IUseCase<String, NewsStoryWrapper> {
    private val tag = GetNewsUseCase::class.simpleName

    override suspend fun invoke(param: String): NewsStoryWrapper {
        val result =
            try {
                retrieveFromRemote(param)
            } catch (throwable: Throwable) {
                if (throwable.message.equals(NewsStoryWrapper.NetworkError.toString())) {
                    NewsStoryWrapper.NetworkError
                } else {
                    NewsStoryWrapper.GenericError
                }
            }

        if (result is NewsStoryWrapper.NetworkError) {
            return retrieveFromLocal()
        }

        return result
    }

    private suspend fun retrieveFromRemote(param: String): NewsStoryWrapper {
        val result =
            remote.getNews(param).also {
                Log.d(tag, "Retrieved from remote")
            }

        saveInDb(result)

        return result
    }

    private suspend fun retrieveFromLocal(): NewsStoryWrapper {
        val result =
            local.getNews().also {
                Log.d(tag, "Retrieved from local")
            }

        return result
    }

    private suspend fun saveInDb(result: NewsStoryWrapper) {
        if (result !is NewsStoryWrapper.Success) {
            return
        }

        try {
            local.removeNews()
            local.saveNews(*result.stories.filter { it.objId.isNotEmpty() }.toTypedArray())
        } catch (throwable: Throwable) {
            Log.d(tag, "Dao exception -> ${throwable.message}")
        }
    }
}
