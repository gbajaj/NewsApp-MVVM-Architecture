package com.gauravbajaj.newsapp.ui.news_sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.data.repository.NewsSourcesRepository
import com.gauravbajaj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsSourcesViewModel @Inject constructor(
    private val repository: NewsSourcesRepository
) : ViewModel() {

    private val _newsSources = MutableLiveData<UiState<List<Source>>>()
    val newsSources: LiveData<UiState<List<Source>>> = _newsSources

    fun loadNewsSources() {
        viewModelScope.launch {
            repository.getNewsSources()
                .onStart { _newsSources.value = UiState.Loading }
                .catch { e ->
                    _newsSources.value = UiState.Error(e.message ?: "Unknown error")
                }
                .collect { sources ->
                    _newsSources.value = UiState.Success(sources)
                }
        }
    }
}
