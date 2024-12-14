package me.fidelep.ravennews.domain.models

sealed class NewsArticleWrapper {
    data class Success(
        val articles: List<NewsArticleModel>,
    ) : NewsArticleWrapper()

    data class Error(
        val code: Int,
        val message: String,
    ) : NewsArticleWrapper()

    object NetworkError : NewsArticleWrapper()

    object GenericError : NewsArticleWrapper()
}
