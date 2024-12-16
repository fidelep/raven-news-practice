package me.fidelep.ravennews.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.usecases.DeleteNewsUseCase
import me.fidelep.ravennews.domain.usecases.GetNewsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {
    @ViewModelScoped
    @Provides
    fun provideGetNewsUseCase(newsRepository: INewsRepository) = GetNewsUseCase(newsRepository)

    @ViewModelScoped
    @Provides
    fun provideDeleteNewsUseCase(newsRepository: INewsRepository) = DeleteNewsUseCase(newsRepository)
}
