package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TtsManager
import com.example.audio.TtsPlaybackState
import com.example.data.repository.NewsRepository
import com.example.model.NewsArticle
import com.example.model.NewsCategory
import com.example.model.NewsSource
import com.example.model.ReaderFontType
import com.example.model.ReaderSettings
import com.example.model.ReaderTheme
import com.example.model.ReadingStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object Feed : Screen()
    data object Explore : Screen()
    data object Bookmarks : Screen()
    data object Stats : Screen()
    data class ArticleDetail(val articleId: String) : Screen()
}

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewsRepository(application)
    val ttsManager = TtsManager(application)

    // Navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Feed)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Navigation back stack for sub-screens
    private val screenBackStack = mutableListOf<Screen>()

    // Selected category filter on feed
    private val _selectedCategory = MutableStateFlow(NewsCategory.ALL)
    val selectedCategory: StateFlow<NewsCategory> = _selectedCategory.asStateFlow()

    // Search query & selected source
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSourceFilter = MutableStateFlow<NewsSource?>(null)
    val selectedSourceFilter: StateFlow<NewsSource?> = _selectedSourceFilter.asStateFlow()

    // Reader settings
    private val _readerSettings = MutableStateFlow(ReaderSettings())
    val readerSettings: StateFlow<ReaderSettings> = _readerSettings.asStateFlow()

    private val _showReaderSettingsSheet = MutableStateFlow(false)
    val showReaderSettingsSheet: StateFlow<Boolean> = _showReaderSettingsSheet.asStateFlow()

    // Saved articles from Room
    val savedArticles: StateFlow<List<NewsArticle>> = repository.getSavedArticlesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Saved article IDs set for fast lookup
    val savedArticleIds: StateFlow<Set<String>> = savedArticles.combine(_selectedCategory) { saved, _ ->
        saved.map { it.id }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Reading stats
    val readingStats: StateFlow<ReadingStats> = repository.readingStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReadingStats())

    // TTS playback state
    val ttsState: StateFlow<TtsPlaybackState> = ttsManager.state

    // All available sources
    val allSources: List<NewsSource> = repository.getAllSources()

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun openArticle(article: NewsArticle) {
        repository.recordArticleRead(article)
        navigateTo(Screen.ArticleDetail(article.id))
    }

    fun navigateBack(): Boolean {
        return if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            true
        } else if (_currentScreen.value !is Screen.Feed) {
            _currentScreen.value = Screen.Feed
            true
        } else {
            false
        }
    }

    fun selectCategory(category: NewsCategory) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectSourceFilter(source: NewsSource?) {
        _selectedSourceFilter.value = if (_selectedSourceFilter.value?.id == source?.id) null else source
    }

    fun getFilteredArticles(): List<NewsArticle> {
        val cat = _selectedCategory.value
        val list = repository.getArticlesByCategory(cat)
        val src = _selectedSourceFilter.value
        return if (src != null) {
            list.filter { it.source.id == src.id }
        } else {
            list
        }
    }

    fun getSearchResults(): List<NewsArticle> {
        val query = _searchQuery.value
        val src = _selectedSourceFilter.value
        val results = repository.searchArticles(query)
        return if (src != null) {
            results.filter { it.source.id == src.id }
        } else {
            results
        }
    }

    fun getArticleById(id: String): NewsArticle? {
        val fromRepo = repository.getArticleById(id)
        if (fromRepo != null) return fromRepo
        return savedArticles.value.find { it.id == id }
    }

    fun toggleSaveArticle(article: NewsArticle) {
        viewModelScope.launch {
            repository.toggleSaveArticle(article)
        }
    }

    fun removeSavedArticle(id: String) {
        viewModelScope.launch {
            repository.removeSavedArticle(id)
        }
    }

    // TTS controls
    fun playArticleTts(article: NewsArticle) {
        ttsManager.playArticle(article)
    }

    fun togglePlayPauseTts() {
        ttsManager.togglePlayPause()
    }

    fun stopTts() {
        ttsManager.stop()
    }

    fun cycleTtsSpeed() {
        ttsManager.cycleSpeed()
    }

    fun prevTtsSection() {
        ttsManager.prevSection()
    }

    fun nextTtsSection() {
        ttsManager.nextSection()
    }

    // Reader settings
    fun setShowReaderSettingsSheet(show: Boolean) {
        _showReaderSettingsSheet.value = show
    }

    fun updateFontSize(delta: Float) {
        val current = _readerSettings.value.fontSizeSp
        val updated = (current + delta).coerceIn(13f, 26f)
        _readerSettings.value = _readerSettings.value.copy(fontSizeSp = updated)
    }

    fun updateFontType(fontType: ReaderFontType) {
        _readerSettings.value = _readerSettings.value.copy(fontType = fontType)
    }

    fun updateReaderTheme(theme: ReaderTheme) {
        _readerSettings.value = _readerSettings.value.copy(theme = theme)
    }

    fun updateLineSpacing(spacing: Float) {
        _readerSettings.value = _readerSettings.value.copy(lineSpacingMultiplier = spacing)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
