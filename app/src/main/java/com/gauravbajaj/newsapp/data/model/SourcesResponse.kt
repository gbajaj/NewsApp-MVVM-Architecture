package com.gauravbajaj.newsapp.data.model

import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("sources")
    val sources: List<Source>? = null,
    
    @SerializedName("code")
    val code: String? = null,
    
    @SerializedName("message")
    val message: String? = null
)
