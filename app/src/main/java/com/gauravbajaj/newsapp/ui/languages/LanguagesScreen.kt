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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.CommonNetworkScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguagesScreen(
    onBackPressed: () -> Unit,
    onDoneClicked: (String) -> Unit,
    viewModel: LanguagesViewModel = hiltViewModel()
) {
    var selectedLanguages by remember { mutableStateOf(emptyList<String>()) }
    val uiState by viewModel.languages.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Initial) {
            viewModel.loadLanguages()
        }
    }
    CommonNetworkScreen(
        title = stringResource(id = R.string.select_language),
        onBackPressed = onBackPressed,
        uiState = uiState as UiState<Any>,
        actions = {
            if (selectedLanguages.isNotEmpty()) {
                TextButton(
                    onClick = {
                        onDoneClicked(selectedLanguages.joinToString(","))
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.action_done),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        onSuccess = { state, modifier ->
            val uiState = state as UiState.Success
            val data = uiState.data as List<Language>
            LanguagesContent(
                languages = data ?: emptyList(),
                selectedLanguages = selectedLanguages,
                onLanguageSelected = {
                    selectedLanguages = if (it in selectedLanguages) {
                        selectedLanguages - it
                    } else {
                        if (selectedLanguages.size < 2) {
                            selectedLanguages + it
                        } else {
                            selectedLanguages
                        }
                    }
                },
                modifier = modifier
            )
        },
        theme = MaterialTheme,
    )
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
