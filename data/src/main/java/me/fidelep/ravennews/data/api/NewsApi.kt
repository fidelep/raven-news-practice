package me.fidelep.ravennews.data.api

import me.fidelep.ravennews.data.api.models.GetNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    companion object {
        const val VERSION = "v1"
    }

    @GET("/$VERSION/search_by_date/search_by_date")
    suspend fun getNews(
        @Query("query") topic: String,
    ): GetNewsResponse
}
