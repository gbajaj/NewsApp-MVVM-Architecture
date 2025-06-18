package com.gauravbajaj.newsapp.navigation


/**
 * Represents the different screens in the application.
 * Each screen has a unique route string that is used for navigation.
 *
 * @property route The unique route string for the screen.
 */
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object TopHeadlines : Screen("top_headlines")
    object NewsSources : Screen("news_sources")
    object NewsList : Screen("news_list")
    object CountrySources : Screen("country_sources")
    object Languages : Screen("languages")
    object Search : Screen("search")
}
