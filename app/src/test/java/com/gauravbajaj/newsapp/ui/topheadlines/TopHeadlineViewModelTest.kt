package com.gauravbajaj.newsapp.ui.topheadlines

import app.cash.turbine.test
import com.gauravbajaj.newsapp.MainCoroutineRule
import com.gauravbajaj.newsapp.data.api.FakeNetworkService
import com.gauravbajaj.newsapp.data.model.Article
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.data.repository.TopHeadlineRepository
import com.gauravbajaj.newsapp.ui.base.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class TopHeadlineViewModelTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val fakeNetworkService = FakeNetworkService()
    private val repository = TopHeadlineRepository(fakeNetworkService)
    private lateinit var viewModel: TopHeadlineViewModel

    @Before
    fun setup() {
        viewModel = TopHeadlineViewModel(repository)
    }

    @Test
    fun loadTopHeadlines_emitsLoadingAndSuccessStates() = runTest {
        // Given
        val testCountry = "us"
        val testArticles = listOf(
            Article(
                source = Source("1", "Test Source"),
                author = "Test Author",
                title = "Test Title",
                description = "Test Description",
                url = "https://test.com",
                urlToImage = "https://test.com/image.jpg",
                publishedAt = "2023-01-01T00:00:00Z",
                content = "Test Content"
            )
        )
        fakeNetworkService.setTopHeadlines(testArticles)

        // When & Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            viewModel.loadTopHeadlines(testCountry)
            val successState = awaitItem() as UiState.Success
            assertEquals(testArticles, successState.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadTopHeadlines_withEmptyCountry_usesDefaultCountry() = runTest {
        // Given
        val testArticles = emptyList<Article>()
        fakeNetworkService.setEmptyTopHeadlines()

        // When & Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            viewModel.loadTopHeadlines("")
            val successState = awaitItem() as UiState.Success
            assertTrue(successState.data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadTopHeadlines_whenNetworkError_emitsErrorState() = runTest {
        // Given
        val testCountry = "us"
        val errorMessage = "Network error"
        fakeNetworkService.setError(RuntimeException(errorMessage))

        // When & Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            viewModel.loadTopHeadlines(testCountry)
            val errorState = awaitItem() as UiState.Error
            assertEquals(errorMessage, errorState.message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadTopHeadlines_withValidData_verifiesArticleDetails() = runTest {
        // Given
        val testArticles = listOf(
            Article(
                source = Source("1", "Real Source"),
                author = "Real Author",
                title = "Real Title",
                description = "Real Description",
                url = "https://real.com",
                urlToImage = "https://real.com/image.jpg",
                publishedAt = "2023-01-01T00:00:00Z",
                content = "Real Content"
            )
        )
        fakeNetworkService.setTopHeadlines(testArticles)

        // When & Then
        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            viewModel.loadTopHeadlines("us")
            val successState = awaitItem() as UiState.Success
            with(successState.data.first()) {
                assertEquals("Real Title", title)
                assertEquals("Real Author", author)
                assertEquals("Real Description", description)
                assertEquals("https://real.com", url)
                assertEquals("https://real.com/image.jpg", urlToImage)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}