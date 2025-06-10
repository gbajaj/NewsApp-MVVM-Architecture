package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.MainCoroutineRule
import com.gauravbajaj.newsapp.data.api.FakeNetworkService
import com.gauravbajaj.newsapp.data.model.Source
import com.gauravbajaj.newsapp.data.model.SourcesResponse
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
class NewsSourcesRepositoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var fakeNetworkService: FakeNetworkService
    private lateinit var repository: NewsSourcesRepository

    private val testSource = Source(
        id = "test-id",
        name = "Test Source",
        description = "Test Description",
        url = "https://test.com",
        category = "general",
        language = "en",
        country = "us"
    )

    @Before
    fun setup() {
        fakeNetworkService = FakeNetworkService()
        repository = NewsSourcesRepository(fakeNetworkService)
    }

    @Test
    fun `getNewsSources returns list of sources on success`() = runTest {
        // Given
        val testSources = listOf(testSource)
        fakeNetworkService.sourcesResponse = SourcesResponse("ok", testSources)

        // When
        val result = repository.getNewsSources().first()

        // Then
        assertEquals(testSources, result)
    }

    @Test
    fun `getNewsSources returns empty list when sources are null`() = runTest {
        // Given
        fakeNetworkService.sourcesResponse = SourcesResponse("ok", null)

        // When
        val result = repository.getNewsSources().first()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test(expected = IOException::class)
    fun `getNewsSources propagates network errors`() = runTest {
        // Given
        fakeNetworkService.exception = IOException("Network error")

        // When
        repository.getNewsSources().first()

        // Then - IOException is expected to be thrown
    }

    @Test(expected = Exception::class)
    fun `getNewsSources throws exception when status is not ok`() = runTest {
        // Given
        fakeNetworkService.sourcesResponse = SourcesResponse("error", null, "API error")

        // When
        repository.getNewsSources().first()

        // Then - Exception is expected to be thrown
    }
}