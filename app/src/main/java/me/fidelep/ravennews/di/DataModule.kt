package me.fidelep.ravennews.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.fidelep.ravennews.BuildConfig
import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.api.repositories.NewsRepository
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideNewsApi(): NewsApi = NewsApi.build("${BuildConfig.NEWS_SERVICE_BASE_URL}/${BuildConfig.BASE_URL_API_VERSION}/")

    @Singleton
    @Provides
    fun providesNewsRepository(newsApi: NewsApi): INewsRepository = NewsRepository(newsApi)
}
