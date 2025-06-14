package com.gauravbajaj.newsapp.ui.news_sources

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.CommonNetworkScreen
import com.gauravbajaj.newsapp.ui.components.LoadingIndicator

import com.gauravbajaj.newsapp.ui.components.EmptyState
import com.gauravbajaj.newsapp.ui.components.ErrorAndRetryState

import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsSourcesScreen(
    onBackPressed: () -> Unit,
    uiState: UiState<List<Source>>,
    loadNewsSources: () -> Unit,
    onSourceClick: (String) -> Unit
) {
    CommonNetworkScreen(
        title = stringResource(id = R.string.news_sources),
        onBackPressed = onBackPressed,
        uiState = uiState,
        onRetry = { loadNewsSources() },
        onSuccess = { state, modifier ->
            val uiState = state as UiState.Success
            val data = uiState.data as List<Source>
            data.let { sources ->
                if (sources.isNotEmpty()) {
                    NewsSourcesList(
                        sources = sources,
                        onSourceClick = { source -> onSourceClick(source.id.toString()) },
                        modifier = modifier
                    )
                } else {
                    EmptyState(
                        message = stringResource(id = R.string.no_sources_found),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        theme = MaterialTheme,
    )
}


@Composable
private fun NewsSourcesList(
    sources: List<Source>,
    onSourceClick: (Source) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sources, key = { it.name }) { source ->
            NewsSourceItem(
                source = source,
                onClick = { onSourceClick(source) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsSourcesListPreview() {
    val sampleSources = listOf(
        Source(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news, analysis, exclusive interviews, and videos at ABCNews.com.",
            url = "https://abcnews.go.com",
            category = "general",
            language = "en",
            country = "us"
        ),
        Source(
            id = "bbc-news",
            name = "BBC News",
            description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
            url = "https://www.bbc.co.uk/news",
            category = "general",
            language = "en",
            country = "gb"
        ),
        Source(
            id = "cnn",
            name = "CNN",
            description = "View the latest news and breaking news today for U.S., world, weather, entertainment, politics and health at CNN.com.",
            url = "https://www.cnn.com",
            category = "general",
            language = "en",
            country = "us"
        )
    )

    NewsAppTheme {
        Surface {
            NewsSourcesList(
                sources = sampleSources,
                onSourceClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsSourceItemPreview() {
    val sampleSource = Source(
        id = "abc-news",
        name = "ABC News",
        description = "Your trusted source for breaking news, analysis, exclusive interviews, and videos at ABCNews.com.",
        url = "https://abcnews.go.com",
        category = "general",
        language = "en",
        country = "us"
    )

    NewsAppTheme {
        Surface {
            NewsSourceItem(
                source = sampleSource,
                onClick = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsSourceItem(
    source: Source,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = source.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (!source.description.isNullOrBlank()) {
                Text(
                    text = source.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(4.dp),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(
                    text = source.category.toString().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
