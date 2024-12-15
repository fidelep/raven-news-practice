package me.fidelep.ravennews.domain.models

sealed class NewsStoryWrapper {
    data class Success(
        val stories: List<NewsStoryModel>,
    ) : NewsStoryWrapper()

    data class Error(
        val code: Int,
        val message: String,
    ) : NewsStoryWrapper()

    object NetworkError : NewsStoryWrapper()

    object GenericError : NewsStoryWrapper()
}
