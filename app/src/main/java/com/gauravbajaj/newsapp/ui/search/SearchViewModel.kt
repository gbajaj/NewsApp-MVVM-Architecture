package com.gauravbajaj.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.ui.base.UiSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
@OptIn(FlowPreview::class)
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
     * Represents the current search query as a StateFlow.
     *
     * This property uses a backing MutableStateFlow [_searchQuery] to store the current search query string.
     * The public [searchQuery] is exposed as an immutable StateFlow, allowing observers to react to changes
     * in the search query without being able to modify it directly.
     *
     * @property _searchQuery A MutableStateFlow that holds the current search query string.
     * @property searchQuery A read-only StateFlow that exposes the current search query to observers.
     */
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery

    /**
     * Initializes the search query flow processing.
     *
     * This function sets up a flow pipeline that processes search queries:
     * 1. Debounces the input to prevent rapid successive searches.
     * 2. Filters out blank queries.
     * 3. Ensures only distinct queries are processed.
     * 4. Triggers a search for each valid query.
     *
     * @param _searchQuery The [MutableStateFlow] containing the current search query.
     * @param viewModelScope The [CoroutineScope] in which the flow will be collected.
     * @param searchNews The function to be called for performing the actual search.
     *
     * Note: This function doesn't return a value, but initiates a continuous flow processing.
     */
    init {
        _searchQuery
            .debounce(300)
            .filterNot { it.isBlank() }
            .distinctUntilChanged()
            .onEach { query ->   // Perform the search when the query changes
                searchNews(query)
            }
            .launchIn(viewModelScope)
    }

    /**
     * Initiates a search for news articles based on the provided query.
     *
     * This function updates the UI state to reflect the current state of the search operation.
     * It starts by setting the state to Loading, then performs the search using the searchRepository.
     * The results are collected and the UI state is updated accordingly:
     * - If articles are found, the state is set to Success with the list of articles.
     * - If no articles are found, the state is set to Error with a "No articles found" message.
     * - If an error occurs during the search, the state is set to Error with the error message.
     *
     * @param query The search term used to find news articles.
     *
     * Note: This function doesn't return a value directly, but updates the [_uiState] property,
     * which can be observed for changes in the UI.
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

    /**
     * Sets the search query for news articles.
     *
     * This function updates the internal [_searchQuery] MutableStateFlow with the provided query string.
     * The updated query will trigger a new search operation due to the Flow collection set up in the
     * ViewModel's initialization block.
     *
     * @param query The search query string to be set. This string will be used to search for news articles.
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

}