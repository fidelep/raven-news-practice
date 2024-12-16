package me.fidelep.ravennews.data.api.models

import com.google.gson.annotations.SerializedName
import me.fidelep.ravennews.domain.models.NewsStoryModel

data class NewsStoryDTO(
    @SerializedName("story_id")
    val storyId: Int,
    @SerializedName("author")
    val author: String,
    @SerializedName("story_title")
    val title: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("story_url")
    val storyUrl: String?,
)

fun NewsStoryDTO.toModel() =
    NewsStoryModel(
        id = storyId,
        author = author,
        title = title ?: "",
        createdAt = createdAt ?: "",
        url = storyUrl ?: "",
    )
