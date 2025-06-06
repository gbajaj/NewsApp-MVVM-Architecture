package com.gauravbajaj.newsapp.data.api

import com.gauravbajaj.newsapp.data.model.TopHeadlinesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NetworkService {

    companion object {
        // Note: In a production app, this should be stored securely using BuildConfig or a secrets management solution
        private const val API_KEY = "YOUR_API_KEY" // Replace with your NewsAPI key
        const val BASE_URL = "https://newsapi.org/v2/"
    }

    @Headers("X-Api-Key: $API_KEY")
    @GET("top-headlines")
    suspend fun getTopHeadlines(@Query("country") country: String): TopHeadlinesResponse
}
