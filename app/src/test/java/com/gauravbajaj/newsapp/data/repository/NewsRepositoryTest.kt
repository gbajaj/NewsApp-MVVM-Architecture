package com.gauravbajaj.newsapp.data.repository

import app.cash.turbine.test
import com.gauravbajaj.newsapp.data.api.FakeNetworkService
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryTest {

    private lateinit var fakeNetworkService: FakeNetworkService
    private lateinit var newsRepository: NewsRepository

    private val testSource = "test-source"
    private val testCountry = "us"
    private val testLanguage = "en"

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

    private val testArticles = listOf(testArticle)

    @Before
    fun setup() {
        fakeNetworkService = FakeNetworkService()
        newsRepository = NewsRepository(fakeNetworkService)
    }

    @Test
    fun `getNews with all parameters should return articles`() = runTest {
        // Given
        fakeNetworkService.setTopHeadlines(testArticles)

        // When
        val result = newsRepository.getNews(testSource, testCountry, testLanguage)

        // Then
        result.collect {
            assertEquals(testArticles, it)
        }
    }

    @Test
    fun `getNews with null parameters should return articles`() = runTest {
        // Given
        fakeNetworkService.setTopHeadlines(testArticles)

        // When
        val result = newsRepository.getNews()

        // Then
        result.collect {
            assertEquals(testArticles, it)
        }
    }


    @Test
    fun `getNews when network throws exception should propagate exception`() = runTest {
        // Given
        fakeNetworkService.setError(IOException("Network error"))

        // When
        newsRepository.getNews(testSource, testCountry, testLanguage).test {
            awaitError() is IOException
        }

        // Then - IOException is expected to be thrown
    }

    @Test
    fun `getNews with empty articles should return empty list`() = runTest {
        // Given
        fakeNetworkService.setEmptyTopHeadlines()

        // When
        val result = newsRepository.getNews(testSource, testCountry, testLanguage)

        // Then
        result.collect {
            assertTrue(it.isEmpty())
        }

    }
}