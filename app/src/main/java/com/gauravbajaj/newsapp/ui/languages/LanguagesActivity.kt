package com.gauravbajaj.newsapp.ui.languages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Language
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguagesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LanguagesScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagesScreen(
    viewModel: LanguagesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val languages by viewModel.languages.collectAsStateWithLifecycle()
    var selectedLanguages by remember { mutableStateOf(emptyList<String>()) }
    
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
                    IconButton(onClick = { (context as? ComponentActivity)?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (selectedLanguages.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                NewsListActivity.start(
                                    context,
                                    language = selectedLanguages.joinToString()
                                )
                                (context as? ComponentActivity)?.finish()
                            }
                        ) {
                            Text(
                                text = "Done",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
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
            val languages = languages as UiState.Success<List<Language>>
            LanguageList(
                languages = languages.data,
                selectedLanguages = selectedLanguages,
                onLanguageSelected = { language ->
                    selectedLanguages = if (language in selectedLanguages) {
                        selectedLanguages - language
                    } else if (selectedLanguages.size < 2) {
                        selectedLanguages + language
                    } else {
                        selectedLanguages
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit,
    isEnabled: Boolean
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentColor = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = language.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Previews
@Preview(showBackground = true)
@Composable
private fun LanguagesScreenPreview() {
    val sampleLanguages = listOf(
        Language("en", "English"),
        Language("es", "Spanish"),
        Language("fr", "French"),
        Language("de", "German"),
        Language("it", "Italian")
    )
    
    NewsAppTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize()) {
                LanguageList(
                    languages = sampleLanguages,
                    selectedLanguages = listOf("en"),
                    onLanguageSelected = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageItemPreview() {
    NewsAppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LanguageItem(
                language = Language("en", "English"),
                isSelected = true,
                onClick = {},
                isEnabled = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            LanguageItem(
                language = Language("es", "Spanish"),
                isSelected = false,
                onClick = {},
                isEnabled = true
            )
        }
    }
}
