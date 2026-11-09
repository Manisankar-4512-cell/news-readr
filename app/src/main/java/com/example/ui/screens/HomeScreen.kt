package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NewsArticle
import com.example.model.NewsCategory
import com.example.ui.components.BreakingTicker
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.HeroNewsCard
import com.example.ui.components.StandardNewsCard
import com.example.ui.theme.AccentBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    articles: List<NewsArticle>,
    breakingArticle: NewsArticle?,
    selectedCategory: NewsCategory,
    savedArticleIds: Set<String>,
    onSelectCategory: (NewsCategory) -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    onToggleSave: (NewsArticle) -> Unit,
    onListenArticle: (NewsArticle) -> Unit,
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateString = remember {
        SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Briefly",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = dateString.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToSearch,
                        modifier = Modifier.testTag("home_search_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search News",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Breaking News Ticker (if available)
            if (breakingArticle != null) {
                item {
                    BreakingTicker(
                        article = breakingArticle,
                        onClick = { onArticleClick(breakingArticle) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // 2. Featured Hero Story (first article)
            if (articles.isNotEmpty()) {
                val hero = articles.first()
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Top Story",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Updated live",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HeroNewsCard(
                            article = hero,
                            isSaved = savedArticleIds.contains(hero.id),
                            onArticleClick = { onArticleClick(hero) },
                            onToggleSave = { onToggleSave(hero) },
                            onListenClick = { onListenArticle(hero) }
                        )
                    }
                }
            }

            // 3. Category Filter Chips
            item {
                Column {
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    CategoryFilterRow(
                        selectedCategory = selectedCategory,
                        onCategorySelected = onSelectCategory
                    )
                }
            }

            // 4. Article Feed List
            val remainingArticles = if (articles.size > 1) articles.drop(1) else articles
            if (remainingArticles.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No articles in this category right now.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(remainingArticles, key = { it.id }) { article ->
                    StandardNewsCard(
                        article = article,
                        isSaved = savedArticleIds.contains(article.id),
                        onArticleClick = { onArticleClick(article) },
                        onToggleSave = { onToggleSave(article) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
