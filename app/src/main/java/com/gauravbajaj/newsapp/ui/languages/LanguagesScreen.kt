package com.gauravbajaj.newsapp.ui.languages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import com.gauravbajaj.newsapp.ui.base.UiState

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
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.select_language)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_back_button_desc)
                        )
                    }
                },
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
                }
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
                    message = state.message ?: stringResource(id = R.string.error_loading_languages),
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
