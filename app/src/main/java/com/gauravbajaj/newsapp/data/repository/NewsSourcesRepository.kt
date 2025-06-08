package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.model.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsSourcesRepository @Inject constructor(
    private val networkService: NetworkService
) {
    suspend fun getNewsSources(): Flow<List<Source>> = flow {
        val response = networkService.getNewsSources()
        if (response.status == "ok") {
            emit(response.sources ?: emptyList())
        } else {
            throw Exception(response.message ?: "Unknown error")
        }
    }
}
