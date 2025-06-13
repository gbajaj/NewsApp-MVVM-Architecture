package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.model.Country
import java.util.regex.Pattern
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CountriesRepositoryTest {

    private lateinit var repository: CountriesRepository

    @Before
    fun setup() {
        repository = CountriesRepository()
    }

    @Test
    fun `getCountries should return a Flow containing the predefined list of countries`() = runTest {
        // When
        val result = repository.getCountries().first()

        // Then
        assertEquals(CountriesRepository.countriesList, result)
        assertEquals(54, result.size)
        assertEquals(Country("ve", "Venezuela", "🇻🇪"), result.last())
        assertEquals(Country("ar", "Argentina", "🇦🇷"), result.first())
    }
    
    @Test
    fun `countriesList should contain exactly 54 countries`() = runTest {
        // When
        val result = repository.getCountries().first()

        // Then
        assertEquals(54, result.size)
    }


    @Test
    fun `each country should have a unique two-letter code`() = runTest {
        // When
        val countries = repository.getCountries().first()

        // Then
        val uniqueCodes = countries.map { it.code }.toSet()
        assertEquals(countries.size, uniqueCodes.size)
    }

    @Test
    fun `all country names should be non-empty strings`() = runTest {
        // When
        val countries = repository.getCountries().first()

        // Then
        countries.forEach { country ->
            assertTrue("Country name should not be empty", country.name.isNotEmpty())
        }
        assertEquals(54, countries.count { it.name.isNotEmpty() })
    }


    @Test
    fun `countriesList should include United States with correct code and flag`() = runTest {
        // When
        val countries = repository.getCountries().first()

        // Then
        val unitedStates = countries.find { it.code == "us" }
        assertNotNull("United States should be in the list", unitedStates)
        assertEquals("United States", unitedStates?.name)
        assertEquals("🇺🇸", unitedStates?.flag)
    }

    @Test
    fun `countriesList should not contain duplicate country names`() = runTest {
        // When
        val countries = repository.getCountries().first()

        // Then
        val uniqueNames = countries.map { it.name }.toSet()
        assertEquals("There should be no duplicate country names", countries.size, uniqueNames.size)
    }

    @Test
    fun `getCountries should emit the list once and complete`() = runTest {
        // Given
        val repository = CountriesRepository()

        // When
        val result = repository.getCountries().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(CountriesRepository.countriesList, result[0])
    }

    // Helper function to check if a string is a valid emoji
    private fun String.isEmoji(): Boolean {
        val emojiPattern = Pattern.compile("^\\p{InEmoticons}$")
        return emojiPattern.matcher(this).matches()
    }
}