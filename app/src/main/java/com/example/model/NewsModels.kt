package com.example.model

import androidx.compose.ui.graphics.Color

enum class NewsCategory(val displayName: String, val chipColor: Long) {
    ALL("All", 0xFF2563EB),
    TECH("Technology", 0xFF0284C7),
    WORLD("World", 0xFFDC2626),
    BUSINESS("Business", 0xFF059669),
    SCIENCE("Science", 0xFF7C3AED),
    CLIMATE("Climate", 0xFF16A34A),
    CULTURE("Culture", 0xFFD97706)
}

data class NewsSource(
    val id: String,
    val name: String,
    val initial: String,
    val website: String,
    val isVerified: Boolean = true
)

data class NewsArticle(
    val id: String,
    val title: String,
    val summary: String,
    val content: List<String>,
    val keyTakeaways: List<String>,
    val category: NewsCategory,
    val source: NewsSource,
    val author: String,
    val publishedTimeAgo: String,
    val readTimeMinutes: Int,
    val imageUrl: String? = null,
    val imageDrawableRes: Int? = null,
    val tags: List<String>,
    val isBreaking: Boolean = false,
    val isSaved: Boolean = false
)

enum class ReaderTheme(val displayName: String, val bgLight: Long, val textLight: Long, val bgDark: Long, val textDark: Long) {
    DEFAULT("Default", 0xFFFFFFFF, 0xFF0F172A, 0xFF0F172A, 0xFFF8FAFC),
    PAPER("Warm Paper", 0xFFFBF0D9, 0xFF2D2319, 0xFF26201A, 0xFFE6D6BD),
    SEPIA("Sepia", 0xFFF4ECD8, 0xFF3E3120, 0xFF2B2418, 0xFFE0D5BE),
    MIDNIGHT("Midnight", 0xFF090D16, 0xFFE2E8F0, 0xFF090D16, 0xFFE2E8F0),
    OLED("Pure Black", 0xFF000000, 0xFFFFFFFF, 0xFF000000, 0xFFE4E4E7)
}

enum class ReaderFontType(val displayName: String) {
    SERIF("Serif (Editorial)"),
    SANS_SERIF("Sans (Modern)"),
    MONOSPACE("Monospace (Code)")
}

data class ReaderSettings(
    val fontSizeSp: Float = 17f,
    val fontType: ReaderFontType = ReaderFontType.SERIF,
    val theme: ReaderTheme = ReaderTheme.DEFAULT,
    val lineSpacingMultiplier: Float = 1.45f
)

data class ReadingStats(
    val articlesReadToday: Int = 3,
    val readingStreakDays: Int = 5,
    val totalReadingMinutes: Int = 42,
    val topicsExploredCount: Int = 6,
    val savedCount: Int = 4
)
