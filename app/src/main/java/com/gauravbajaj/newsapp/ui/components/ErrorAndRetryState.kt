package com.gauravbajaj.newsapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme

/**
 * A composable function that displays an error state with an optional retry button.
 *
 * This function creates a column layout that shows an error message and, if provided,
 * a retry button. It's typically used to inform the user about an error and give them
 * the option to retry the action that caused the error.
 *
 * @param message The error message to be displayed to the user.
 * @param onRetry An optional lambda function that will be called when the retry button is clicked.
 *                If null, the retry button will not be shown.
 * @param modifier A [Modifier] that will be applied to the root Column composable. Defaults to [Modifier].
 *
 * @return A composable that displays the error state UI.
 */
@Composable
fun ErrorAndRetryState(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
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
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (onRetry != null) {
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview() {
    NewsAppTheme {
        ErrorAndRetryState(
            message = "Failed to load articles. Please try again.",
            onRetry = {}
        )
    }
}