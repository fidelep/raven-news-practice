package me.fidelep.ravennews.data.api.models

import com.google.gson.annotations.SerializedName

data class GetNewsResponse(
    @SerializedName("hits")
    val articles: List<NewsArticleDTO>,
)
