package com.gauravbajaj.newsapp.ui.country_sources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Country
import com.gauravbajaj.newsapp.data.repository.CountriesRepository
import com.gauravbajaj.newsapp.di.BackgroundContext
import com.gauravbajaj.newsapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for [CountrySourcesActivity]
 *
 * @author Gaurav Bajaj
 */
@HiltViewModel
class CountrySourcesViewModel @Inject constructor(
    val countriesRepository: CountriesRepository,
    @BackgroundContext val dispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val _countries = MutableStateFlow<UiState<List<Country>>>(UiState.Initial)
    val countries: StateFlow<UiState<List<Country>>> = _countries

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            try {
                _countries.value = UiState.Loading
                countriesRepository.getCountries().flowOn(dispatcher).collect { countriesList ->
                    countriesList.sortedBy { it.name }
                    _countries.value = UiState.Success(countriesList)
                }
            } catch (e: Exception) {
                _countries.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
