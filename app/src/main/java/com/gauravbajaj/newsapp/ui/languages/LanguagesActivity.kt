package com.gauravbajaj.newsapp.ui.languages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguagesActivity : ComponentActivity() {
    private val viewModel: LanguagesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.languages.collectAsStateWithLifecycle()
                    var selectedLanguages by remember { mutableStateOf(emptyList<String>()) }
                    LaunchedEffect(uiState) {
                        if (uiState is UiState.Initial) {
                            viewModel.loadLanguages()
                        }
                    }
                    LanguagesScreen(
                        onBackPressed = { onBackPressed() },
                        uiState = uiState,
                        selectedLanguages = selectedLanguages,
                        onLanguageSelected = {
                            selectedLanguages = if (it in selectedLanguages) {
                                selectedLanguages - it
                            } else {
                                if (selectedLanguages.size < 2) {
                                    selectedLanguages + it
                                } else {
                                    selectedLanguages
                                }
                            }
                        },
                        onDoneClicked = { NewsListActivity.start(this@LanguagesActivity) }
                    )
                }
            }
        }
    }
}

