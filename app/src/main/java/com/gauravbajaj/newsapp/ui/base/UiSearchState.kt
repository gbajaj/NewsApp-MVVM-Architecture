package com.gauravbajaj.newsapp.ui.base

/**
 * A generic class that holds a value with its loading status for the search results.
 */
sealed class UiSearchState<out T> {
    data class Success<out T>(val data: T) : UiSearchState<T>()
    data class Error(val message: String? = null) : UiSearchState<Nothing>()
    object Loading : UiSearchState<Nothing>()
    object Empty : UiSearchState<Nothing>()
}
