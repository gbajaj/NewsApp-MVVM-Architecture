package com.gauravbajaj.newsapp.data.repository

import com.gauravbajaj.newsapp.data.model.Language
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguagesRepositoryTest {

    private lateinit var repository: LanguagesRepository

    @Before
    fun setup() {
        repository = LanguagesRepository()
    }

    @Test
    fun `getLanguages should return a flow containing the correct list of 10 languages`() = runTest {
        // When
        val result = repository.getLanguages().first()

        // Then
        assertEquals(10, result.size)
        assertEquals(LanguagesRepository.languagesList, result)
        
        // Verify specific languages
        assertEquals(Language("en", "English", "English", "🇬🇧"), result[0])
        assertEquals(Language("es", "Spanish", "Español", "🇪🇸"), result[1])
        assertEquals(Language("fr", "French", "Français", "🇫🇷"), result[2])
        assertEquals(Language("de", "German", "Deutsch", "🇩🇪"), result[3])
        assertEquals(Language("hi", "Hindi", "हिंदी", "🇮🇳"), result[4])
        assertEquals(Language("ar", "Arabic", "العربية", "🇸🇦"), result[5])
        assertEquals(Language("zh", "Chinese", "中文", "🇨🇳"), result[6])
        assertEquals(Language("ja", "Japanese", "日本語", "🇯🇵"), result[7])
        assertEquals(Language("ru", "Russian", "Русский", "🇷🇺"), result[8])
        assertEquals(Language("pt", "Portuguese", "Português", "🇵🇹"), result[9])
    }

    @Test
    fun `getLanguages should return a list with unique language codes`() = runTest {
        // Given
        val repository = LanguagesRepository()

        // When
        val languages = repository.getLanguages().first()

        // Then
        val uniqueLanguageCodes = languages.map { it.code }.toSet()
        assertEquals(languages.size, uniqueLanguageCodes.size)
    }

    @Test
    fun `getLanguages should ensure each language has a non-empty native name`() = runTest {
        // When
        val languages = repository.getLanguages().first()

        // Then
        languages.forEach { language ->
            assertTrue("Native name for ${language.code} should not be empty", language.nativeName.isNotEmpty())
        }
    }

    @Test
    fun `languagesList should have all unique flag emojis`() = runTest {
        // When
        val languages = repository.getLanguages().first()

        // Then
        val flagEmojis = languages.map { it.flag }.distinct()
        assertEquals(languages.size, flagEmojis.distinct().size)
        
        // Verify specific flag emojis
        assertTrue(flagEmojis.contains("🇬🇧"))
        assertTrue(flagEmojis.contains("🇪🇸"))
        assertTrue(flagEmojis.contains("🇫🇷"))
        assertTrue(flagEmojis.contains("🇩🇪"))
        assertTrue(flagEmojis.contains("🇮🇳"))
        assertTrue(flagEmojis.contains("🇸🇦"))
        assertTrue(flagEmojis.contains("🇨🇳"))
        assertTrue(flagEmojis.contains("🇯🇵"))
        assertTrue(flagEmojis.contains("🇷🇺"))
        assertTrue(flagEmojis.contains("🇵🇹"))
    }

    @Test
    fun `getLanguages should return a Flow object`() = runTest {
        // Given
        val repository = LanguagesRepository()

        // When
        val result = repository.getLanguages()

        // Then
        assertTrue(result is Flow<List<Language>>)
    }

    @Test
    fun `languagesList should be immutable`() = runTest {
        // Given
        val originalList = repository.getLanguages().first()
        
        // When
        val mutableList = originalList.toMutableList()
        mutableList.add(Language("test", "Test", "Test", "🏴"))
        
        // Then
        val updatedList = repository.getLanguages().first()
        assertEquals(originalList, updatedList)
        assertNotEquals(mutableList, updatedList)
        assertEquals(10, updatedList.size)
    }
}