package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val networkService: NetworkService
) {
    suspend fun getNews(
        source: String? = null,
        country: String? = null,
        language: String? = null
    ): Flow<List<Article>>  = flow  {
        try {
            val response = networkService.getTopHeadlines(
                country = country?: "",
                sources = source?: "",
                language = language?: "",
            )
            emit(response.articles)
        } catch (e: Exception) {
            // Log the error or handle it as needed
            throw e
        }
    }
}
