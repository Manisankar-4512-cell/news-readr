package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Feed
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Feed
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.NewsViewModel
import com.example.ui.Screen
import com.example.ui.components.AudioPlayerBar
import com.example.ui.components.ReaderSettingsSheet
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: NewsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val selectedCategory by viewModel.selectedCategory.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val selectedSource by viewModel.selectedSourceFilter.collectAsState()
                val savedArticles by viewModel.savedArticles.collectAsState()
                val savedArticleIds by viewModel.savedArticleIds.collectAsState()
                val readingStats by viewModel.readingStats.collectAsState()
                val ttsState by viewModel.ttsState.collectAsState()
                val readerSettings by viewModel.readerSettings.collectAsState()
                val showSettingsSheet by viewModel.showReaderSettingsSheet.collectAsState()
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                // Handle system back navigation
                BackHandler(enabled = currentScreen !is Screen.Feed) {
                    viewModel.navigateBack()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        Column(modifier = Modifier.navigationBarsPadding()) {
                            // Audio Mini Player Bar (shown whenever an article is queued or playing)
                            AudioPlayerBar(
                                ttsState = ttsState,
                                onTogglePlayPause = { viewModel.togglePlayPauseTts() },
                                onNextSection = { viewModel.nextTtsSection() },
                                onPrevSection = { viewModel.prevTtsSection() },
                                onCycleSpeed = { viewModel.cycleTtsSpeed() },
                                onClose = { viewModel.stopTts() },
                                onClickBar = {
                                    ttsState.currentArticle?.let { article ->
                                        viewModel.openArticle(article)
                                    }
                                }
                            )

                            // Show standard Bottom Navigation Bar on main tabs
                            if (currentScreen !is Screen.ArticleDetail) {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    tonalElevation = 6.dp,
                                    modifier = Modifier.testTag("bottom_nav_bar")
                                ) {
                                    NavigationBarItem(
                                        selected = currentScreen is Screen.Feed,
                                        onClick = { viewModel.navigateTo(Screen.Feed) },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentScreen is Screen.Feed) Icons.Default.Feed else Icons.Outlined.Feed,
                                                contentDescription = "Feed"
                                            )
                                        },
                                        label = { Text("Feed") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AccentBlue,
                                            selectedTextColor = AccentBlue,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_feed")
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen is Screen.Explore,
                                        onClick = { viewModel.navigateTo(Screen.Explore) },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentScreen is Screen.Explore) Icons.Default.Explore else Icons.Outlined.Explore,
                                                contentDescription = "Explore"
                                            )
                                        },
                                        label = { Text("Explore") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AccentBlue,
                                            selectedTextColor = AccentBlue,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_explore")
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen is Screen.Bookmarks,
                                        onClick = { viewModel.navigateTo(Screen.Bookmarks) },
                                        icon = {
                                            BadgedBox(
                                                badge = {
                                                    if (savedArticles.isNotEmpty()) {
                                                        Badge { Text("${savedArticles.size}") }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (currentScreen is Screen.Bookmarks) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                                    contentDescription = "Saved"
                                                )
                                            }
                                        },
                                        label = { Text("Saved") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AccentBlue,
                                            selectedTextColor = AccentBlue,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_saved")
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen is Screen.Stats,
                                        onClick = { viewModel.navigateTo(Screen.Stats) },
                                        icon = {
                                            Icon(
                                                imageVector = if (currentScreen is Screen.Stats) Icons.Default.Insights else Icons.Outlined.Insights,
                                                contentDescription = "Insights"
                                            )
                                        },
                                        label = { Text("Insights") },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = AccentBlue,
                                            selectedTextColor = AccentBlue,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                        ),
                                        modifier = Modifier.testTag("nav_stats")
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        when (val screen = currentScreen) {
                            is Screen.Feed -> {
                                val articles = viewModel.getFilteredArticles()
                                val breaking = articles.firstOrNull { it.isBreaking }
                                HomeScreen(
                                    articles = articles,
                                    breakingArticle = breaking,
                                    selectedCategory = selectedCategory,
                                    savedArticleIds = savedArticleIds,
                                    onSelectCategory = { viewModel.selectCategory(it) },
                                    onArticleClick = { viewModel.openArticle(it) },
                                    onToggleSave = { viewModel.toggleSaveArticle(it) },
                                    onListenArticle = { viewModel.playArticleTts(it) },
                                    onNavigateToSearch = { viewModel.navigateTo(Screen.Explore) }
                                )
                            }

                            is Screen.Explore -> {
                                val results = viewModel.getSearchResults()
                                ExploreScreen(
                                    searchQuery = searchQuery,
                                    sources = viewModel.allSources,
                                    selectedSource = selectedSource,
                                    searchResults = results,
                                    savedArticleIds = savedArticleIds,
                                    onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                    onSelectSource = { viewModel.selectSourceFilter(it) },
                                    onArticleClick = { viewModel.openArticle(it) },
                                    onToggleSave = { viewModel.toggleSaveArticle(it) }
                                )
                            }

                            is Screen.Bookmarks -> {
                                BookmarksScreen(
                                    savedArticles = savedArticles,
                                    onArticleClick = { viewModel.openArticle(it) },
                                    onRemoveSaved = { viewModel.removeSavedArticle(it) },
                                    onExploreClick = { viewModel.navigateTo(Screen.Feed) }
                                )
                            }

                            is Screen.Stats -> {
                                StatsScreen(
                                    stats = readingStats,
                                    savedCount = savedArticles.size,
                                    onOpenReaderSettings = { viewModel.setShowReaderSettingsSheet(true) }
                                )
                            }

                            is Screen.ArticleDetail -> {
                                val article = viewModel.getArticleById(screen.articleId)
                                if (article != null) {
                                    val isCurrentPlaying = ttsState.isPlaying && ttsState.currentArticle?.id == article.id
                                    ArticleDetailScreen(
                                        article = article,
                                        isSaved = savedArticleIds.contains(article.id),
                                        isTtsPlaying = isCurrentPlaying,
                                        settings = readerSettings,
                                        onBack = { viewModel.navigateBack() },
                                        onToggleSave = { viewModel.toggleSaveArticle(article) },
                                        onToggleTts = {
                                            if (isCurrentPlaying) {
                                                viewModel.togglePlayPauseTts()
                                            } else {
                                                viewModel.playArticleTts(article)
                                            }
                                        },
                                        onOpenReaderSettings = { viewModel.setShowReaderSettingsSheet(true) }
                                    )
                                } else {
                                    viewModel.navigateBack()
                                }
                            }
                        }

                        // Reader Appearance Customizer Sheet
                        if (showSettingsSheet) {
                            ReaderSettingsSheet(
                                settings = readerSettings,
                                sheetState = sheetState,
                                onDismiss = { viewModel.setShowReaderSettingsSheet(false) },
                                onFontSizeDelta = { viewModel.updateFontSize(it) },
                                onFontTypeChange = { viewModel.updateFontType(it) },
                                onThemeChange = { viewModel.updateReaderTheme(it) },
                                onLineSpacingChange = { viewModel.updateLineSpacing(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
