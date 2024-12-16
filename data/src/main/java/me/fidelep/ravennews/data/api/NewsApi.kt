package me.fidelep.ravennews.data.api

import me.fidelep.ravennews.data.api.models.GetNewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    companion object {
        fun build(url: String): NewsApi =
            Retrofit
                .Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
    }

    @GET("search_by_date")
    suspend fun getNews(
        @Query("query") topic: String,
    ): GetNewsResponse
}
