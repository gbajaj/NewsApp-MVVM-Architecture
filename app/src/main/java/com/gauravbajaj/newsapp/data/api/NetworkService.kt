package com.gauravbajaj.newsapp.data.api

import com.gauravbajaj.newsapp.data.model.SourcesResponse
import com.gauravbajaj.newsapp.data.model.TopHeadlinesResponse
import com.gauravbajaj.newsapp.utils.AppConstant.API_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String): TopHeadlinesResponse

    @Headers("X-Api-Key: $API_KEY")
    @GET("sources")
    suspend fun getNewsSources(): SourcesResponse
    
    @Headers("X-Api-Key: $API_KEY")
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): TopHeadlinesResponse
}