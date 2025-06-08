package com.gauravbajaj.newsapp.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.NewsRepository
import com.gauravbajaj.newsapp.ui.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    private var currentSource: String? = null
    private var currentCountry: String? = null
    private var currentLanguage: String? = null

    fun loadNews(source: String? = null, country: String? = null, language: String? = null) {
        currentSource = source ?: currentSource
        currentCountry = country ?: currentCountry
        currentLanguage = language ?: currentLanguage
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val articles = repository.getNews(
                    source = currentSource,
                    country = currentCountry,
                    language = currentLanguage
                )
                _uiState.value = UiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun refresh() {
        loadNews()
    }
}
