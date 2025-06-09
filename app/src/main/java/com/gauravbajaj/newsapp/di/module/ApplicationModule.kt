package com.gauravbajaj.newsapp.di.module

import com.gauravbajaj.newsapp.data.api.NetworkService
import com.gauravbajaj.newsapp.data.repository.NewsRepository
import com.gauravbajaj.newsapp.data.repository.NewsSourcesRepository
import com.gauravbajaj.newsapp.data.repository.SearchRepository
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.di.BaseUrl
import com.gauravbajaj.newsapp.utils.AppConstant.API_KEY
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule() {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", API_KEY)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    @Provides
    @Singleton
    fun provideNetworkService(@BaseUrl baseUrl: String, retrofit: Retrofit): NetworkService {
        return retrofit
            .create(NetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Provides
    @Singleton
    fun provideNewsRepository(networkService: NetworkService) = NewsRepository(networkService)

    @Provides
    @Singleton
    fun provideNewsSourcesRepository(networkService: NetworkService) =
        NewsSourcesRepository(networkService)

    @Provides
    @Singleton
    fun provideSearchRepository(networkService: NetworkService) = SearchRepository(networkService)

    @Provides
    @Singleton
    fun provideTopHeadlineRepository(networkService: NetworkService) =
        TopHeadlineRepository(networkService)

}