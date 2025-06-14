package com.gauravbajaj.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gauravbajaj.newsapp.ui.components.CommonTopBar
import com.gauravbajaj.newsapp.ui.country_sources.CountrySourcesActivity
import com.gauravbajaj.newsapp.ui.languages.LanguagesActivity
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesActivity
import com.gauravbajaj.newsapp.ui.search.SearchActivity
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlineActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopBar(
                text = LocalContext.current.resources.getString(R.string.app_name),
                theme = MaterialTheme
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Rounded button with 15% margin on both sides
            MainScreenButton(text = LocalContext.current.resources.getString(R.string.top_headlines)) {
                context.startActivity(Intent(context, TopHeadlineActivity::class.java))
            }
            MainScreenButton(text = LocalContext.current.resources.getString(R.string.news_sources)) {
                context.startActivity(Intent(context, NewsSourcesActivity::class.java))
            }

            MainScreenButton(text = LocalContext.current.resources.getString(R.string.countries)) {
                context.startActivity(Intent(context, CountrySourcesActivity::class.java))
            }

            MainScreenButton(text = LocalContext.current.resources.getString(R.string.languages)) {
                context.startActivity(Intent(context, LanguagesActivity::class.java))
            }

            MainScreenButton(text = LocalContext.current.resources.getString(R.string.search)) {
                context.startActivity(Intent(context, SearchActivity::class.java))
            }
        }
    }
}

@Composable
fun MainScreenButton(text: String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.85f)  // 85% width (15% margin on each side)
            .height(56.dp * 1.2f)  // 20% taller than default Material button height
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,  // Rounded corners
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {

        Text(text = text)

    }
}
