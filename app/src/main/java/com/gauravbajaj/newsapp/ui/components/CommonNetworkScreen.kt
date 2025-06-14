package com.gauravbajaj.newsapp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.ui.base.UiState
/**
 * A composable function that creates a common network screen with various states handling.
 *
 * This function sets up a scaffold with a top bar and handles different UI states
 * (Initial, Success, Loading, Error) based on the provided [uiState].
 *
 * @param title The title to be displayed in the top bar.
 * @param onBackPressed A lambda function to be invoked when the back button is pressed.
 * @param onRetry An optional lambda function to be invoked when retry is requested in the error state.
 * @param actions A composable function that defines additional actions in the top bar.
 * @param uiState The current UI state of type [UiState] that determines which composable to display.
 * @param onInitial A composable function to be invoked when the UI state is Initial.
 * @param onSuccess A composable function to be invoked when the UI state is Success.
 * @param theme The [MaterialTheme] to be applied to the screen.
 */
@Composable
fun CommonNetworkScreen(
    title: String,
    onBackPressed: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    uiState: UiState<Any>,
    onInitial: @Composable (modifier: Modifier) -> Unit = {},
    onSuccess: @Composable (uiState: UiState<Any>, modifier: Modifier) -> Unit = {state, modifier -> },
    theme: MaterialTheme
) {
    Scaffold(
        topBar = {
            CommonTopBar(
                text = title,
                onBackClick = onBackPressed,
                actions = actions, theme = theme,
            )
        }
    ) { padding ->
        when (uiState) {
            is UiState.Initial -> {
                onInitial(Modifier.padding(padding))
            }

            is UiState.Success -> {
                onSuccess(uiState, Modifier.padding(padding))
            }

            is UiState.Loading -> {
                LoadingIndicator(Modifier.padding(padding))
            }

            is UiState.Error -> {
                ErrorAndRetryState(
                    message = (uiState as UiState.Error).message
                        ?: stringResource(id = R.string.error_loading_content),
                    onRetry = onRetry,
                    modifier = Modifier.padding(16.dp)
                )
            }

            else -> {}
        }
    }
}