package com.gauravbajaj.newsapp.data.api

import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.data.model.SourcesResponse
import com.gauravbajaj.newsapp.data.model.TopHeadlinesResponse
import javax.inject.Inject

/**
 * A fake implementation of [NetworkService] for testing purposes.
 * Allows controlling the responses and simulating different scenarios.
 */

class FakeNetworkService @Inject constructor() : NetworkService {

    // Properties to control the behavior of the fake
    private var topHeadlinesResponse: TopHeadlinesResponse = TopHeadlinesResponse("ok", 0, emptyList())
    internal var sourcesResponse: SourcesResponse = SourcesResponse("ok", emptyList())
    internal var exception: Exception? = null

    // Test data
    private val testArticle = Article(
        source = Source("test-id", "Test Source"),
        author = "Test Author",
        title = "Test Title",
        description = "Test Description",
        url = "https://test.com",
        urlToImage = "https://test.com/image.jpg",
        publishedAt = "2023-01-01T00:00:00Z",
        content = "Test Content"
    )

    // Setters for test control
    fun setTopHeadlines(articles: List<Article>) {
        this.topHeadlinesResponse = TopHeadlinesResponse("ok", articles.size, articles)
    }

    fun setEmptyTopHeadlines() {
        this.topHeadlinesResponse = TopHeadlinesResponse("ok", 0, emptyList())
    }

    // Add to FakeNetworkService.kt
    fun setSources(sources: List<Source>) {
        this.sourcesResponse = SourcesResponse("ok", sources)
    }

    fun setSourcesResponse(response: SourcesResponse) {
        this.sourcesResponse = response
    }

    fun setError(exception: Exception) {
        this.exception = exception
    }

    // NetworkService implementation
    override suspend fun getTopHeadlines(
        country: String,
        sources: String,
        language: String
    ): TopHeadlinesResponse {
        exception?.let { throw it }
        return topHeadlinesResponse
    }

    override suspend fun getNewsSources(): SourcesResponse {
        exception?.let { throw it }
        return sourcesResponse
    }

    override suspend fun searchNews(
        query: String,
        pageSize: Int,
        page: Int,
        sortBy: String
    ): TopHeadlinesResponse {
        exception?.let { throw it }
        return topHeadlinesResponse
    }

    // Helper methods for test setup
    companion object {
        fun createWithTestData(articles: List<Article> = emptyList()): FakeNetworkService {
            return FakeNetworkService().apply {
                if (articles.isNotEmpty()) {
                    setTopHeadlines(articles)
                } else {
                    setEmptyTopHeadlines()
                }
            }
        }

        fun createWithError(exception: Exception): FakeNetworkService {
            return FakeNetworkService().apply {
                setError(exception)
            }
        }
    }
}
