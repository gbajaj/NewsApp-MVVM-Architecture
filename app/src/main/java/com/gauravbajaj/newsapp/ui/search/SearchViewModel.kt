package com.gauravbajaj.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.ui.base.UiSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel class for handling search functionality.
 *
 * This class is responsible for managing the UI-related data for the search screen
 * and interacting with the [SearchRepository] to fetch news articles based on a query.
 *
 * @property searchRepository The repository used to perform the news search.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiSearchState<List<Article>>>(UiSearchState.Empty)
    /**
     * A [StateFlow] representing the current state of the UI.
     * It emits [UiSearchState] objects that encapsulate the different states of the search screen,
     * such as loading, success with a list of articles, error, or empty state.
     */
    val uiState: StateFlow<UiSearchState<List<Article>>> = _uiState

    /**
     * Searches for news articles based on the provided query.
     *
     * This function updates the [_uiState] to reflect the current state of the search operation.
     * It starts with [UiSearchState.Loading], then performs the search using the [searchRepository].
     * On success, it updates the [_uiState] with [UiSearchState.Success] containing the list of articles,
     * or [UiSearchState.Error] if no articles are found. If an error occurs during the search,
     * it updates the [_uiState] with [UiSearchState.Error] containing the error message.
     *
     * @param query The search query.
     */
    fun searchNews(query: String) {
        _uiState.value = UiSearchState.Loading
        
        viewModelScope.launch {
            searchRepository.searchNews(query = query)
                .catch { e ->
                    _uiState.value = UiSearchState.Error(e.message ?: "An unknown error occurred")
                }
                .collect { articles ->
                    _uiState.value = if (articles.isNotEmpty()) {
                        UiSearchState.Success(articles)
                    } else {
                        UiSearchState.Error("No articles found")
                    }
                }
        }
    }
}
