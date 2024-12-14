package me.fidelep.ravennews.data.api.models

import com.google.gson.annotations.SerializedName
import me.fidelep.ravennews.domain.models.NewsArticleModel

data class NewsArticleDTO(
    @SerializedName("author")
    val author: String,
    @SerializedName("story_title")
    val title: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("story_url")
    val storyUrl: String,
)

fun NewsArticleDTO.toModel() =
    NewsArticleModel(
        author = author,
        title = title,
        createdAt = createdAt,
        storyUrl = storyUrl,
    )
