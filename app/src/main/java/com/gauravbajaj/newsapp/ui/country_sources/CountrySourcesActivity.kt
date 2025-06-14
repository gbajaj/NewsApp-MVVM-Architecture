package com.gauravbajaj.newsapp.ui.country_sources

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gauravbajaj.newsapp.R
import com.gauravbajaj.newsapp.data.model.Country
import com.gauravbajaj.newsapp.ui.base.UiState
import com.gauravbajaj.newsapp.ui.components.LoadingIndicator
import com.gauravbajaj.newsapp.ui.components.CommonTopBar
import com.gauravbajaj.newsapp.ui.components.EmptyState
import com.gauravbajaj.newsapp.ui.components.ErrorAndRetryState
import com.gauravbajaj.newsapp.ui.newslist.NewsListActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity to show the list of countries
 *
 * @author Gaurav Bajaj
 */
@AndroidEntryPoint
class CountrySourcesActivity : ComponentActivity() {
    private val viewModel: CountrySourcesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountrySourcesScreen(
                        onBackClick = { onBackPressed() },
                        onCountryClick = { country ->
                            // Open NewsListActivity with the selected country
                            NewsListActivity.start(
                                this@CountrySourcesActivity,
                                country = country.code.lowercase()
                            )
                        },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountrySourcesScreen(
    onBackClick: () -> Unit,
    onCountryClick: (Country) -> Unit,
    viewModel: CountrySourcesViewModel
) {
    val countriesState by viewModel.countries.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadCountries()
    }

    Scaffold(
        topBar = {
            CommonTopBar(
                text = stringResource(R.string.select_country),
                onBackClick = onBackClick,
                theme = MaterialTheme,
            )
        }
    ) { padding ->
        when (val state = countriesState) {
            is UiState.Loading -> {
                LoadingIndicator(padding)
            }
            is UiState.Success -> {
                val countries = state.data ?: emptyList()
                if (countries.isEmpty()) {
                    EmptyState(message = stringResource(R.string.no_countries_available),
                        modifier = Modifier.padding(padding))
                } else {
                    CountryList(
                        countries = countries,
                        onCountryClick = onCountryClick,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
            is UiState.Error -> {
                ErrorAndRetryState(
                    message = state.message ?: stringResource(R.string.error_loading_countries),
                    onRetry = { viewModel.loadCountries() },
                    modifier = Modifier.padding(padding)
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun CountryList(
    countries: List<Country>,
    onCountryClick: (Country) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(countries) { country ->
            CountryItem(
                country = country,
                onClick = { onCountryClick(country) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CountryItem(
    country: Country,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = country.flag,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = country.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f)
            )
        }
    }
}