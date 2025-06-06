package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.data.model.SourcesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

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
