package com.gauravbajaj.newsapp.ui.topheadlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.di.BackgroundContext
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.base.UiState.Initial
import com.gauravbajaj.newsapp.utils.AppConstant.COUNTRY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.flowOn

@HiltViewModel
class TopHeadlineViewModel @Inject constructor(
    private val repository: TopHeadlineRepository,
    @BackgroundContext private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(
        Initial
    )
    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    fun loadTopHeadlines(country: String = COUNTRY) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getTopHeadlines(country)
                .onStart { _uiState.value = UiState.Loading }.catch {
                _uiState.value = UiState.Error(it.message)
            }.flowOn(dispatcher).collect { articles ->
                _uiState.value = UiState.Success(articles)
            }
        }
    }
}
