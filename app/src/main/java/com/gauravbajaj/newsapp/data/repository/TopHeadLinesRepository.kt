package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.model.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TopHeadlineRepository @Inject constructor(
    private val networkService: NetworkService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun getTopHeadlines(country: String): Flow<List<Article>> = flow {
        // Perform API call and map the response to a list of articles
        val articles = networkService.getTopHeadlines(country).articles
        // Emit the list of articles as a Flow
        emit(articles)
    }.flowOn(ioDispatcher)
}
