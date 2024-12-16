package me.fidelep.ravennews.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.db.dao.NewsStoryDao
import me.fidelep.ravennews.data.repositories.LocalNewsRepository
import me.fidelep.ravennews.data.repositories.RemoteNewsRepository
import me.fidelep.ravennews.domain.interfaces.INewsPreferences
import me.fidelep.ravennews.domain.usecases.DeleteNewsUseCase
import me.fidelep.ravennews.domain.usecases.GetNewsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @ViewModelScoped
    @Provides
    fun provideGetNewsUseCase(
        remoteRepository: RemoteNewsRepository,
        localRepository: LocalNewsRepository,
    ) = GetNewsUseCase(remoteRepository, localRepository)

    @ViewModelScoped
    @Provides
    fun provideDeleteNewsUseCase(newsRepository: LocalNewsRepository) = DeleteNewsUseCase(newsRepository)

    @ViewModelScoped
    @Provides
    fun providesRemoteNewsRepository(
        newsApi: NewsApi,
        newsPreferences: INewsPreferences,
    ): RemoteNewsRepository = RemoteNewsRepository(newsApi, newsPreferences)

    @ViewModelScoped
    @Provides
    fun providesLocalNewsRepository(
        newsStoryDao: NewsStoryDao,
        newsPreferences: INewsPreferences,
    ): LocalNewsRepository = LocalNewsRepository(newsStoryDao, newsPreferences)
}
