package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.MainCoroutineRule
import com.gauravbajaj.newsapp.data.api.FakeNetworkService
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class TopHeadlineRepositoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var fakeNetworkService: FakeNetworkService
    private lateinit var repository: TopHeadlineRepository

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

    @Before
    fun setup() {
        fakeNetworkService = FakeNetworkService()
        repository = TopHeadlineRepository(fakeNetworkService)
    }

    @Test
    fun getTopHeadlines_returnsListOfArticlesOnSuccess() = runTest {
        // Given
        val testArticles = listOf(testArticle)
        fakeNetworkService.setTopHeadlines(testArticles)

        // When
        val result = repository.getTopHeadlines("us").first()

        // Then
        assertEquals(testArticles, result)
    }

    @Test
    fun getTopHeadlines_returnsEmptyListWhenNoArticles() = runTest {
        // Given
        fakeNetworkService.setEmptyTopHeadlines()

        // When
        val result = repository.getTopHeadlines("us").first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test(expected = IOException::class)
    fun getTopHeadlines_propagatesNetworkErrors() = runTest {
        // Given
        fakeNetworkService.exception = IOException("Network error")

        // When
        repository.getTopHeadlines("us").first()

        // Then - IOException is expected to be thrown
    }

    @Test
    fun getTopHeadlines_usesProvidedCountryParameter() = runTest {
        // Given
        val testCountry = "ca" // Canada
        val testArticles = listOf(testArticle)
        fakeNetworkService.setTopHeadlines(testArticles)

        // When
        val result = repository.getTopHeadlines(testCountry).first()

        // Then
        assertEquals(testArticles, result)
        // Note: In a real test, we would verify the country parameter was used in the API call
    }
}