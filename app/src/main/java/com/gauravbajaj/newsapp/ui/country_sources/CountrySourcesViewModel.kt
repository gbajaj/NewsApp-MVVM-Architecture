package com.gauravbajaj.newsapp.ui.country_sources

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.data.model.Country
import com.gauravbajaj.newsapp.ui.base.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * View model for [CountrySourcesActivity]
 *
 * @author Gaurav Bajaj
 */
class CountrySourcesViewModel @Inject constructor() : ViewModel() {

    private val _countries = MutableLiveData<UiState<List<Country>>>()
    val countries: LiveData<UiState<List<Country>>> = _countries

    init {
        loadCountries()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            try {
                _countries.value = UiState.Loading
                val countriesList = listOf(
                    Country("ae", "United Arab Emirates", "🇦🇪"),
                    Country("ar", "Argentina", "🇦🇷"),
                    Country("at", "Austria", "🇦🇹"),
                    Country("au", "Australia", "🇦🇺"),
                    Country("be", "Belgium", "🇧🇪"),
                    Country("bg", "Bulgaria", "🇧🇬"),
                    Country("br", "Brazil", "🇧🇷"),
                    Country("ca", "Canada", "🇨🇦"),
                    Country("ch", "Switzerland", "🇨🇭"),
                    Country("cn", "China", "🇨🇳"),
                    Country("co", "Colombia", "🇨🇴"),
                    Country("cu", "Cuba", "🇨🇺"),
                    Country("cz", "Czech Republic", "🇨🇿"),
                    Country("de", "Germany", "🇩🇪"),
                    Country("eg", "Egypt", "🇪🇬"),
                    Country("fr", "France", "🇫🇷"),
                    Country("gb", "United Kingdom", "🇬🇧"),
                    Country("gr", "Greece", "🇬🇷"),
                    Country("hk", "Hong Kong", "🇭🇰"),
                    Country("hu", "Hungary", "🇭🇺"),
                    Country("id", "Indonesia", "🇮🇩"),
                    Country("ie", "Ireland", "🇮🇪"),
                    Country("il", "Israel", "🇮🇱"),
                    Country("in", "India", "🇮🇳"),
                    Country("it", "Italy", "🇮🇹"),
                    Country("jp", "Japan", "🇯🇵"),
                    Country("kr", "South Korea", "🇰🇷"),
                    Country("lt", "Lithuania", "🇱🇹"),
                    Country("lv", "Latvia", "🇱🇻"),
                    Country("ma", "Morocco", "🇲🇦"),
                    Country("mx", "Mexico", "🇲🇽"),
                    Country("my", "Malaysia", "🇲🇾"),
                    Country("ng", "Nigeria", "🇳🇬"),
                    Country("nl", "Netherlands", "🇳🇱"),
                    Country("no", "Norway", "🇳🇴"),
                    Country("nz", "New Zealand", "🇳🇿"),
                    Country("ph", "Philippines", "🇵🇭"),
                    Country("pl", "Poland", "🇵🇱"),
                    Country("pt", "Portugal", "🇵🇹"),
                    Country("ro", "Romania", "🇷🇴"),
                    Country("rs", "Serbia", "🇷🇸"),
                    Country("ru", "Russia", "🇷🇺"),
                    Country("sa", "Saudi Arabia", "🇸🇦"),
                    Country("se", "Sweden", "🇸🇪"),
                    Country("sg", "Singapore", "🇸🇬"),
                    Country("si", "Slovenia", "🇸🇮"),
                    Country("sk", "Slovakia", "🇸🇰"),
                    Country("th", "Thailand", "🇹🇭"),
                    Country("tr", "Turkey", "🇹🇷"),
                    Country("tw", "Taiwan", "🇹🇼"),
                    Country("ua", "Ukraine", "🇺🇦"),
                    Country("us", "United States", "🇺🇸"),
                    Country("ve", "Venezuela", "🇻🇪"),
                    Country("za", "South Africa", "🇿🇦")
                ).sortedBy { it.name }
                _countries.value = UiState.Success(countriesList)
            } catch (e: Exception) {
                _countries.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}
