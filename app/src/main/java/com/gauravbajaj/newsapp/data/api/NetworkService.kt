package com.gauravbajaj.newsapp.data.api

import com.gauravbajaj.newsapp.data.model.SourcesResponse
import com.gauravbajaj.newsapp.data.model.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String = "us",
                                @Query("sources") sources: String = "",
                                @Query("language") language: String = "en",
                                ): TopHeadlinesResponse

    @GET("sources")
    suspend fun getNewsSources(): SourcesResponse
    
    @GET("everything")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): TopHeadlinesResponse
}