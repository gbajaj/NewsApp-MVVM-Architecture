package com.gauravbajaj.newsapp.ui.topheadlines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TopHeadlineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    TopHeadlinesScreen(
                        onBackPressed = { onBackPressed() },
                        onArticleClick = { url ->
                            CustomTabsHelper.launchUrl(context, url)
                        }
                    )
                }
            }
        }
    }
}