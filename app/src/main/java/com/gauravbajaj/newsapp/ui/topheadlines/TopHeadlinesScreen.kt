package com.gauravbajaj.newsapp.ui.topheadlines

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.CommonNetworkScreen
import com.gauravbajaj.newsapp.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopHeadlinesScreen(
    onBackPressed: (() -> Unit)? = null,
    uiState: UiState<List<Article>>,
    loadTopHeadlines: () -> Unit,
    onArticleClick: (String) -> Unit
) {
    CommonNetworkScreen(
        title = stringResource(id = R.string.select_language),
        onBackPressed = onBackPressed,
        uiState = uiState,
        onRetry = loadTopHeadlines,
        onSuccess = { state, modifier ->
            val uiState = state as UiState.Success
            val data = uiState.data as List<Article>
            uiState.data.let { sources ->
                if (sources.isNotEmpty()) {
                    ArticleList(
                        articles = uiState.data,
                        modifier = modifier,
                        onArticleClick = { article ->
                            onArticleClick(article.url)
                        }
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
fun ArticleList(
    articles: List<Article>,
    modifier: Modifier = Modifier,
    onArticleClick: (Article) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(articles, key = { it.url }) { article ->
            ArticleItem(
                article = article,
                onClick = { onArticleClick(article) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleItem(
    article: Article,
    onClick: () -> Unit = {}
) {
    val imageModifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(4.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            article.urlToImage?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(
                        model = url,
                        placeholder = painterResource(id = R.drawable.ic_placeholder),
                        error = painterResource(id = R.drawable.ic_placeholder)
                    ),
                    contentDescription = null,
                    modifier = imageModifier,
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

            if (!article.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.source.name ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
