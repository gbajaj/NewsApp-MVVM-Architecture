package com.gauravbajaj.newsapp.ui.topheadlines

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.LoadingIndicator
import com.gauravbajaj.newsapp.ui.components.CommonTopBar
import com.gauravbajaj.newsapp.ui.components.ErrorAndRetryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopHeadlinesScreen(
    onBackPressed: () -> Unit,
    uiState: UiState<List<Article>>,
    loadTopHeadlines: () -> Unit,
    onArticleClick: (String) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CommonTopBar(
                text = stringResource(R.string.top_headlines),
                onBackClick = { (context as? ComponentActivity)?.onBackPressed() },
                theme = MaterialTheme,
            )
        }
    ) { padding ->
        TopHeadlinesContent(
            uiState = uiState,
            padding = padding,
            onRetry = loadTopHeadlines,
            onArticleClick = onArticleClick,
        )
    }
}

@Composable
fun TopHeadlinesContent(
    uiState: UiState<List<Article>>,
    padding: PaddingValues,
    onRetry: () -> Unit,
    onArticleClick: (String) -> Unit,
) {
    val context = LocalContext.current

    when (uiState) {
        is UiState.Success -> {
            ArticleList(
                articles = uiState.data,
                modifier = Modifier.padding(padding),
                onArticleClick = { article ->
                    onArticleClick(article.url)
                }
            )
        }

        is UiState.Loading -> {
            LoadingIndicator(padding)
        }

        is UiState.Error -> {
            ErrorAndRetryState(
                message = uiState.message ?: context.getString(R.string.error_loading_news),
                onRetry = onRetry,
                modifier = Modifier.padding(padding)
            )
        }

        else -> {}
    }
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
