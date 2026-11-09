package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.NewsRepository
import com.example.model.NewsCategory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var repository: NewsRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = NewsRepository(context)
    }

    @Test
    fun `read app name string from context`() {
        val appName = context.getString(R.string.app_name)
        assertEquals("Briefly", appName)
    }

    @Test
    fun `verify articles are loaded and category filtering functions`() {
        val allArticles = repository.getAllArticles()
        assertTrue("Articles list should not be empty", allArticles.isNotEmpty())

        val techArticles = repository.getArticlesByCategory(NewsCategory.TECH)
        assertTrue("Tech articles should be found", techArticles.isNotEmpty())
        assertTrue("All filtered articles must be TECH", techArticles.all { it.category == NewsCategory.TECH })
    }

    @Test
    fun `verify search finds relevant articles`() {
        val searchResults = repository.searchArticles("Quantum")
        assertTrue("Search for Quantum should yield results", searchResults.isNotEmpty())
        assertTrue(searchResults.any { it.title.contains("Quantum", ignoreCase = true) })
    }

    @Test
    fun `verify bookmark toggle persistence in repository`() = runBlocking {
        val sample = repository.getAllArticles().first()
        // Ensure not saved initially
        repository.removeSavedArticle(sample.id)
        assertFalse(repository.isArticleSaved(sample.id))

        // Toggle save
        val saved = repository.toggleSaveArticle(sample)
        assertTrue("Article should now be saved", saved)
        assertTrue(repository.isArticleSaved(sample.id))

        // Toggle again to remove
        val unsaved = repository.toggleSaveArticle(sample)
        assertFalse("Article should now be unsaved", unsaved)
        assertFalse(repository.isArticleSaved(sample.id))
    }
}
