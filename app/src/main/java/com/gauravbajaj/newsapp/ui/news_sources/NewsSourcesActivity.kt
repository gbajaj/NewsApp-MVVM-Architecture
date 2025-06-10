package com.gauravbajaj.newsapp.ui.news_sources

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsSourcesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsSourcesScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsSourcesScreen(
    viewModel: NewsSourcesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.newsSources.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadNewsSources()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.news_sources)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { (context as? ComponentActivity)?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is UiState.Success -> {
                state.data?.let { sources ->
                    if (sources.isNotEmpty()) {
                        NewsSourcesList(
                            sources = sources,
                            onSourceClick = { source ->
                                NewsListActivity.start(context, source = source.id)
                            },
                            modifier = Modifier.padding(padding)
                        )
                    } else {
                        ErrorState(
                            message = stringResource(id = R.string.no_sources_found),
                            onRetry = { viewModel.loadNewsSources() },
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                ErrorState(
                    message = state.message ?: stringResource(id = R.string.error_loading_sources),
                    onRetry = { viewModel.loadNewsSources() },
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {}
        }
    }
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

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}
