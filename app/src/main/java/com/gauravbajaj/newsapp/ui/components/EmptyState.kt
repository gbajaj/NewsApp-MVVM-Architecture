package com.gauravbajaj.newsapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme

/**
 * A composable function that displays an empty state with a centered message.
 *
 * This function creates a full-screen box with a centered text message, typically used
 * to indicate that no content is available or to display a placeholder message.
 *
 * @param message The text message to be displayed in the empty state.
 * @param modifier An optional [Modifier] to be applied to the root Box composable.
 *                 Defaults to [Modifier].
 */
@Composable
internal fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
    NewsAppTheme {
        ErrorAndRetryState(message = "No articles found")
    }
}