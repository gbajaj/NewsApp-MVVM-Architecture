package com.gauravbajaj.newsapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.di.BackgroundContext
import com.gauravbajaj.newsapp.ui.base.UiSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okio.IOException
import javax.inject.Inject

/**
 * ViewModel class for handling search functionality.
 *
 * This class is responsible for managing the UI-related data for the search screen
 * and interacting with the [SearchRepository] to fetch news articles based on a query.
 *
 * It uses Kotlin Flows to handle asynchronous operations and UI state updates in a reactive manner.
 * The search query is debounced and filtered to optimize performance and prevent unnecessary API calls.
 *
 * Key features:
 * - **Debounced search**: Prevents rapid API calls while the user is typing.
 * - **State management**: Uses [UiSearchState] to represent different UI states (Loading, Success, Error, Empty).
 * - **Error handling**: Catches exceptions during the search process and updates the UI accordingly.
 * - **Flow-based architecture**: Leverages Kotlin Flows for managing asynchronous data streams.
 *
 * @property searchRepository The repository responsible for fetching news articles.
 * @property _uiState A [MutableStateFlow] that holds the current UI state.
 * @property uiState A read-only [StateFlow] that exposes the UI state to observers.
 * @property _searchQuery A [MutableStateFlow] that holds the current search query.
 * @property searchQuery A read-only [StateFlow] that exposes the search query to observers.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    @BackgroundContext private val dispatcher: CoroutineDispatcher
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

    init {
        createSearchQueryFlow()
    }

    /**
     * Creates and launches a flow that processes search queries.
     *
     * This function sets up a reactive pipeline for handling search queries:
     * 1. **Debounce**: It waits for 300 milliseconds after the last query change before processing. This prevents excessive API calls during typing.
     * 2. **Filter**: It ignores blank queries (empty or whitespace-only).
     * 3. **Distinct**: It only processes a query if it's different from the previous one.
     * 4. **flatMapLatest**: When a valid, distinct query is received, it cancels any ongoing search and starts a new one by calling `searchNewsFlow(query)`.
     * 5. **Catch**: It handles exceptions that might occur during the search operation (e.g., network errors from the repository).
     *    - If an `IOException` occurs, it clears the search query and restarts the flow, effectively allowing the user to retry the search.
     *    - For other errors, it updates the `_uiState` to reflect the error.
     * 6. **onEach**: For each list of articles emitted by `searchNewsFlow`, it updates the `_uiState`.
     *    - If the list is not empty, it sets the state to `UiSearchState.Success`.
     *    - If the list is empty, it sets the state to `UiSearchState.Empty`.
     * 7. **launchIn**: The entire flow is launched within the `viewModelScope`, ensuring it's managed by the ViewModel's lifecycle.
     */
    private fun createSearchQueryFlow() {
        _searchQuery
            .debounce(300)
            .filterNot { it.isBlank() }
            .distinctUntilChanged()
            .flatMapLatest { query ->// Cancel any ongoing search and start a new one
                // Perform the search when the query changes
                // Now searchNews returns a Flow
                searchNewsFlow(query)
            }
            .catch { e -> // Catch errors from the repository flow here
                if (e is IOException) {
                    setSearchQuery("")
                    // Retry the search if an IOException occurs
                    createSearchQueryFlow()
                }
                _uiState.value =
                    UiSearchState.Error(e.message ?: "An unknown error occurred in repository")
            }
            .onEach { articles -> // Process the emitted articles
                _uiState.value = if (articles.isNotEmpty()) {
                    UiSearchState.Success(articles)
                } else {
                    UiSearchState.Empty // Or Error("No articles found") depending on desired behavior
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Returns a Flow of news articles based on the provided query.
     *
     * This function performs the search using the searchRepository and returns the Flow
     * of articles. It also sets the initial loading state.
     *
     * @param query The search term used to find news articles.
     * @return A Flow emitting a list of Article objects.
     */
    private fun searchNewsFlow(query: String): Flow<List<Article>> {
        _uiState.value = UiSearchState.Loading // Set loading state before starting the flow
        return searchRepository.searchNews(query = query).flowOn(dispatcher)
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