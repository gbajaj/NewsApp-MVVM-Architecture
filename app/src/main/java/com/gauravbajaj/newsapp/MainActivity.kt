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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.gauravbajaj.newsapp.navigation.Screen
import com.gauravbajaj.newsapp.ui.components.CommonTopBar
import com.gauravbajaj.newsapp.ui.country_sources.CountrySourcesScreen
import com.gauravbajaj.newsapp.ui.languages.LanguagesScreen
import com.gauravbajaj.newsapp.ui.news_sources.NewsSourcesScreen
import com.gauravbajaj.newsapp.ui.search.SearchScreen
import com.gauravbajaj.newsapp.ui.theme.NewsAppTheme
import com.gauravbajaj.newsapp.ui.topheadlines.TopHeadlinesScreen
import com.gauravbajaj.newsapp.ui.newslist.NewsListScreen
import com.gauravbajaj.newsapp.utils.CustomTabsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNewsApp()
                }
            }
        }
    }
}

@Composable
fun MyNewsApp() {
    val navController = rememberNavController() // NavController is created here
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.TopHeadlines.route) {
            TopHeadlinesScreen(
                onBackPressed = { navController.popBackStack() },
                onArticleClick = { url -> CustomTabsHelper.launchUrl(context, url) },
            )
        }
        composable(Screen.NewsSources.route) {
            NewsSourcesScreen(
                onBackPressed = { navController.popBackStack() },
                onSourceClick = {
                    navController.navigate(Screen.NewsList.route + "?source=$it")
                },
            )
        }
        composable(
            route = "${Screen.NewsList.route}?source={source}"
        ) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source")
            NewsListScreen(
                onBackClick = { navController.popBackStack() },
                source = source,
            )
        }

        composable(
            route = "${Screen.NewsList.route}?country={country}"
        ) { backStackEntry ->
            val country = backStackEntry.arguments?.getString("country")
            NewsListScreen(
                onBackClick = { navController.popBackStack() },
                country = country,
            )
        }

        composable(
            route = "${Screen.NewsList.route}?language={language}"
        ) { backStackEntry ->
            val language = backStackEntry.arguments?.getString("language")
            NewsListScreen(
                onBackClick = { navController.popBackStack() },
                language = language,
            )
        }
        composable(Screen.CountrySources.route) {
            CountrySourcesScreen(
                onBackClick = { navController.popBackStack() },
                onCountryClick = { country ->
                    navController.navigate("${Screen.NewsList.route}?country=${country.code.lowercase()}")
                })
        }
        composable(Screen.Languages.route) {
            LanguagesScreen(
                onBackPressed = { navController.popBackStack() },
                onDoneClicked = { language ->
                    navController.navigate("${Screen.NewsList.route}?language=${language.lowercase()}")
                }
            )
        }
        composable(Screen.Search.route) {
            SearchScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
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
            MainScreenButton(
                text = LocalContext.current.resources.getString(R.string.top_headlines),
                screen = Screen.TopHeadlines,
                navController = navController
            )
            MainScreenButton(
                text = LocalContext.current.resources.getString(R.string.news_sources),
                screen = Screen.NewsSources,
                navController = navController
            )
            MainScreenButton(
                text = LocalContext.current.resources.getString(R.string.countries),
                screen = Screen.CountrySources,
                navController = navController
            )
            MainScreenButton(
                text = LocalContext.current.resources.getString(R.string.languages),
                screen = Screen.Languages,
                navController = navController
            )
            MainScreenButton(
                text = LocalContext.current.resources.getString(R.string.search),
                screen = Screen.Search,
                navController = navController
            )
        }
    }
}

////                LanguagesScreen(navController)
////            }
////            composable(Screen.Search.route) {
////                SearchScreen(navController)
////            }
//        }
//    }
//}
//
@Composable
fun MainScreenButton(
    text: String,
    screen: Screen,
    navController: NavController
) {
    Button(
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
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
