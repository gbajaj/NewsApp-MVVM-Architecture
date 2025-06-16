package com.gauravbajaj.newsapp.ui.news_sources

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSourcesActivity : ComponentActivity() {
    private val viewModel: NewsSourcesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by viewModel.newsSources.collectAsStateWithLifecycle()

            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(uiState) {
                        if (uiState is UiState.Initial) {
                            viewModel.loadNewsSources()
                        }
                    }
                    NewsSourcesScreen(
                        onBackPressed = { onBackPressed() },
                        loadNewsSources = { viewModel.loadNewsSources() },
                        onSourceClick = { sourceId ->
                            NewsListActivity.start(this@NewsSourcesActivity, source = sourceId)
                        }
                    )
                }
            }
        }
    }
}
