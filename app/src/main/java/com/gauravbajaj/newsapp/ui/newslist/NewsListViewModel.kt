package com.gauravbajaj.newsapp.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.NewsRepository
import com.gauravbajaj.newsapp.di.BackgroundContext
import com.gauravbajaj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val repository: NewsRepository,
    @BackgroundContext private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Initial)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    private var currentSource: String? = null
    private var currentCountry: String? = null
    private var currentLanguage: String? = null

    fun loadNews(source: String? = null, country: String? = null, language: String? = null) {
        currentSource = source ?: currentSource
        currentCountry = country ?: currentCountry
        
        val languages = language?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }?.take(2) ?: emptyList()
        currentLanguage = languages.firstOrNull()
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
                val articles = if (languages.size >= 2) {
                    // If we have exactly 2 languages, fetch news for both and combine the results
                    val firstLangNews = repository.getNews(
                        source = currentSource,
                        country = currentCountry,
                        language = languages[0]
                    )
                    val secondLangNews = repository.getNews(
                        source = currentSource,
                        country = currentCountry,
                        language = languages[1]
                    )
                    // Combine and remove duplicates based on article URL
                    firstLangNews.zip ( secondLangNews ) { first, second ->

                        return@zip  (first + second).distinctBy { it.url }.sortedByDescending { it.publishedAt }
                    }.flowOn(dispatcher)
                } else {
                    // Single language or no language specified
                    repository.getNews(
                        source = currentSource,
                        country = currentCountry,
                        language = currentLanguage
                    ).flowOn(dispatcher)
                }.catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
                }
                    .collect { articles ->
                    _uiState.value = UiState.Success(articles)
                }
        }
    }

    fun refresh() {
        loadNews()
    }
}
