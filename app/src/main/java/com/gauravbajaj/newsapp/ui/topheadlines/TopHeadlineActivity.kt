package com.gauravbajaj.newsapp.ui.topheadlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TopHeadlineActivity : ComponentActivity() {
    private val viewModel by viewModels<TopHeadlineViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                    LaunchedEffect(viewModel.uiState) {
                        if (uiState.value == UiState.Initial) {
                            viewModel.loadTopHeadlines()
                        }
                    }
                    TopHeadlinesScreen(
                        onBackPressed = { onBackPressed() },
                        uiState = uiState.value,
                        loadTopHeadlines = { viewModel.loadTopHeadlines() },
                        onArticleClick = { url -> })
                }
            }
        }
    }
}