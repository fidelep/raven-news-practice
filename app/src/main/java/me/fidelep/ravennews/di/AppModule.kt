package me.fidelep.ravennews.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.fidelep.ravennews.BuildConfig
import me.fidelep.ravennews.data.NewsSharedPreferences
import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.api.interceptors.INetworkInterceptor
import me.fidelep.ravennews.data.api.interceptors.NetworkInterceptor
import me.fidelep.ravennews.data.db.NewsDb
import me.fidelep.ravennews.data.db.dao.NewsStoryDao
import me.fidelep.ravennews.domain.interfaces.INewsPreferences
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, NewsDb::class.java, NewsDb.DB_NAME).build()

    @Singleton
    @Provides
    fun provideNewsDao(database: NewsDb): NewsStoryDao = database.newsDao()

    @Singleton
    @Provides
    fun providesNetworkInterceptor(
        @ApplicationContext context: Context,
    ): INetworkInterceptor = NetworkInterceptor(context)

    @Singleton
    @Provides
    fun geNetworkClient(networkInterceptor: INetworkInterceptor): OkHttpClient = NewsApi.buildNetworkInterceptor(networkInterceptor)

    @Singleton
    @Provides
    fun provideNewsApi(networkClient: OkHttpClient): NewsApi =
        NewsApi.build(
            networkClient,
            "${BuildConfig.NEWS_SERVICE_BASE_URL}/${BuildConfig.BASE_URL_API_VERSION}/",
        )

    @Singleton
    @Provides
    fun provideNewsPreferences(
        @ApplicationContext context: Context,
    ): INewsPreferences = NewsSharedPreferences(context)
}
