package com.gauravbajaj.newsapp.data.model

import com.google.gson.annotations.SerializedName

data class TopHeadlinesResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("totalResults")
    val totalResults: Int,
    
    @SerializedName("articles")
    val articles: List<Article> = emptyList()
)
