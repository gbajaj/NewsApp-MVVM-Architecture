package com.gauravbajaj.newsapp.ui.languages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.LoadingIndicator
import com.gauravbajaj.newsapp.ui.components.CommonTopBar
import com.gauravbajaj.newsapp.ui.components.ErrorAndRetryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguagesScreen(
    onBackPressed: () -> Unit,
    uiState: UiState<List<Language>>,
    selectedLanguages: List<String>,
    onLanguageSelected: (String) -> Unit,
    onDoneClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            CommonTopBar(
                text = stringResource(id = R.string.select_language),
                onBackClick = onBackPressed,
                actions = {
                    if (selectedLanguages.isNotEmpty()) {
                        TextButton(
                            onClick = onDoneClicked
                        ) {
                            Text(
                                text = stringResource(id = R.string.action_done),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }, theme = MaterialTheme
            )
        }
    ) { padding ->

        when (val state = uiState) {
            is UiState.Success -> {
                LanguagesContent(
                    languages = state.data ?: emptyList(),
                    selectedLanguages = selectedLanguages,
                    onLanguageSelected = onLanguageSelected,
                    modifier = Modifier.padding(padding)
                )
            }

            is UiState.Loading -> {
                LoadingIndicator(padding)
            }

            is UiState.Error -> {
                ErrorAndRetryState(
                    message = state.message
                        ?: stringResource(id = R.string.error_loading_languages),
                    onRetry = { /* Handle retry if needed */ },
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {}
        }
    }
}

@Composable
private fun LanguagesContent(
    languages: List<Language>,
    selectedLanguages: List<String>,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (selectedLanguages.isNotEmpty()) {
            Text(
                text = stringResource(
                    id = R.string.selected_languages,
                    selectedLanguages.joinToString()
                ),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Text(
                text = stringResource(id = R.string.select_up_to_two_languages),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        LanguageList(
            languages = languages,
            selectedLanguages = selectedLanguages,
            onLanguageSelected = onLanguageSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LanguageList(
    languages: List<Language>,
    selectedLanguages: List<String>,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(languages) { language ->
            LanguageItem(
                language = language,
                isSelected = language.code in selectedLanguages,
                onClick = { onLanguageSelected(language.code) },
                isEnabled = language.code in selectedLanguages || selectedLanguages.size < 2
            )
        }
    }
}

@Composable
private fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        enabled = isEnabled,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = language.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = language.nativeName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
