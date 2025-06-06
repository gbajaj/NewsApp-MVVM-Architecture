package com.gauravbajaj.newsapp.ui.base

/**
 * A generic class that holds a value with its loading status.
 */
sealed class UiState<out T> {
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}
