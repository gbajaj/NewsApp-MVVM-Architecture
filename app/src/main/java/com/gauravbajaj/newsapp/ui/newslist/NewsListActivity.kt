package com.gauravbajaj.newsapp.ui.newslist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.CommonNetworkScreen
import com.gauravbajaj.newsapp.ui.components.EmptyState
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import com.gauravbajaj.newsapp.utils.formatDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val source = intent.getStringExtra(EXTRA_SOURCE)
        val country = intent.getStringExtra(EXTRA_COUNTRY)
        val language = intent.getStringExtra(EXTRA_LANGUAGE)

        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NewsListScreen(
                        source = source,
                        country = country,
                        language = language,
                        onBackClick = { onBackPressed() }
                    )
                }
            }
        }
    }

    companion object {
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_COUNTRY = "extra_country"
        private const val EXTRA_LANGUAGE = "extra_language"

        fun start(
            context: Context,
            source: String? = null,
            country: String? = null,
            language: String? = null
        ) {
            val intent = Intent(context, NewsListActivity::class.java).apply {
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_COUNTRY, country)
                putExtra(EXTRA_LANGUAGE, language)
            }
            context.startActivity(intent)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    source: String?,
    country: String?,
    language: String?,
    onBackClick: () -> Unit,
    viewModel: NewsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    CommonNetworkScreen(
        title = stringResource(id = R.string.news_sources),
        onBackPressed = onBackClick,
        uiState = uiState as UiState<Any>,
        onRetry = { viewModel.loadNews(source, country, language) },
        onSuccess = { state, modifier ->
            val uiState = state as UiState.Success
            val data = uiState.data as List<Article>
            data.let { sources ->
                if (sources.isNotEmpty()) {
                    NewsList(
                        articles = sources,
                        onArticleClick = { article ->
                            CustomTabsHelper.launchUrl(context, article.url)
                        },
                        modifier = modifier
                    )
                } else {
                    EmptyState(
                        message = stringResource(id = R.string.no_sources_found),
                        modifier = modifier
                    )
                }
            }
        },
        theme = MaterialTheme,
    )
}

@Composable
fun NewsList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles, key = { it.url }) { article ->
            NewsArticleItem(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsArticleItem(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            article.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        error = painterResource(id = R.drawable.ic_launcher_foreground)
                    ),
                    contentDescription = article.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            article.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = article.source.name ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                article.publishedAt.let { date ->
                    Text(
                        text = formatDate(date),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewsArticleItemPreview() {
    NewsAppTheme {
        NewsArticleItem(
            article = Article(
                source = Source("source-id", "Source Name"),
                author = "Author Name",
                title = "Sample News Article Title That Can Be Quite Long and Might Wrap to Multiple Lines",
                description = "This is a sample news article description that provides a brief summary of the article content. It can be longer and might wrap to multiple lines.",
                url = "https://example.com",
                urlToImage = "https://picsum.photos/800/450",
                publishedAt = "2023-06-09T12:34:56Z",
                content = "Full content of the article would go here..."
            ),
            onClick = {}
        )
    }
}

