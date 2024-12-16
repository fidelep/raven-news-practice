package me.fidelep.ravennews.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.fidelep.ravennews.domain.models.NewsStoryModel
import me.fidelep.ravennews.domain.models.NewsStoryWrapper
import me.fidelep.ravennews.domain.usecases.DeleteNewsUseCase
import me.fidelep.ravennews.domain.usecases.GetNewsUseCase
import me.fidelep.ravennews.ui.states.UiEvents
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val deleteNewsUseCase: DeleteNewsUseCase,
) : ViewModel() {
    private val defaultTopic = "android"

    private val _newsState = MutableSharedFlow<List<NewsStoryModel>>()
    val newsState = _newsState.asSharedFlow()

    private val _uiState = MutableStateFlow<UiEvents>(UiEvents.LOADING)
    val uiState = _uiState.asStateFlow()

    fun getNews(topic: String = defaultTopic) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(UiEvents.LOADING)

            val result = getNewsUseCase(topic)

            when (result) {
                is NewsStoryWrapper.Success -> {
                    _newsState.emit(result.stories)
                }

                is NewsStoryWrapper.Error -> {
                    _uiState.emit(UiEvents.ERROR(result.code, result.message))
                }

                is NewsStoryWrapper.GenericError -> {
                    _uiState.emit(UiEvents.ERROR(0x01))
                }

                is NewsStoryWrapper.NetworkError -> {
                    _uiState.emit(UiEvents.ERROR(0x02))
                }
            }.also {
                _uiState.emit(UiEvents.IDLE)
            }
        }
    }

    fun removeNew(storyId: Int) {
        viewModelScope
            .launch(Dispatchers.IO) {
                val wasSaved = deleteNewsUseCase(storyId)

                if (!wasSaved) {
                    _uiState.emit(UiEvents.ERROR(0x03))
                }

                _uiState.emit(UiEvents.IDLE)
            }
    }
}
