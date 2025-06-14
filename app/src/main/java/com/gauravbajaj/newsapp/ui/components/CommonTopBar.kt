package com.gauravbajaj.newsapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R

/**
 * Creates a common top app bar with centered title, back navigation, and optional actions.
 *
 * This composable function generates a [CenterAlignedTopAppBar] with a customizable title,
 * back navigation button, and optional action buttons. It uses Material 3 design components
 * and applies the provided theme colors.
 *
 * @param text The text to be displayed as the title in the top app bar.
 * @param onBackClick A lambda function to be invoked when the back button is clicked.
 * @param actions An optional composable lambda that defines additional action buttons
 *                to be displayed in the top app bar. Defaults to an empty lambda.
 * @param theme The [MaterialTheme] to be applied to the top app bar for consistent styling.
 *
 * @return A composable [CenterAlignedTopAppBar] with the specified properties.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    text: String,
    actions: @Composable RowScope.() -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    theme: MaterialTheme = MaterialTheme
) {

    CenterAlignedTopAppBar(
        title = {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = theme.colorScheme.primary,
            titleContentColor = theme.colorScheme.onPrimary,
            actionIconContentColor = theme.colorScheme.onPrimary,
            navigationIconContentColor = theme.colorScheme.onPrimary
        ),
        navigationIcon = if (onBackClick != null) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_back_button_desc)
                    )
                }
            }
        } else {
            {}
        },
        actions = actions
    )
}