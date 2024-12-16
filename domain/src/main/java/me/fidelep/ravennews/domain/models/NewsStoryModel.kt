package me.fidelep.ravennews.domain.models

data class NewsStoryModel(
    val objId: String,
    val id: Int,
    val author: String,
    val title: String,
    val createdAt: String,
    val url: String,
)
