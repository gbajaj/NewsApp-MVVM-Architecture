package com.gauravbajaj.newsapp.ui.languages

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _languages = MutableLiveData<List<Language>>()
    val languages: LiveData<List<Language>> = _languages

    init {
        loadLanguages()
    }

    fun loadLanguages() {
        // Load languages from a data source (e.g., API, database) and update the _languages LiveData with the sorted list
        viewModelScope.launch {
            val languagesList = listOf(
                Language("en", context.getString(R.string.english), "English", "🇬🇧"),
                Language("es", context.getString(R.string.spanish), "Español", "🇪🇸"),
                Language("fr", context.getString(R.string.french), "Français", "🇫🇷"),
                Language("de", context.getString(R.string.german), "Deutsch", "🇩🇪"),
                Language("hi", context.getString(R.string.hindi), "हिंदी", "🇮🇳"),
                Language("ar", context.getString(R.string.arabic), "العربية", "🇸🇦"),
                Language("zh", context.getString(R.string.chinese), "中文", "🇨🇳"),
                Language("ja", context.getString(R.string.japanese), "日本語", "🇯🇵"),
                Language("ru", context.getString(R.string.russian), "Русский", "🇷🇺"),
                Language("pt", context.getString(R.string.portuguese), "Português", "🇵🇹")
            )
            _languages.value = languagesList.sortedBy { it.name }
        }
    }
}
