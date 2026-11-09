package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles")
data class SavedArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val contentRaw: String, // newline separated paragraphs
    val keyTakeawaysRaw: String, // newline separated points
    val categoryName: String,
    val sourceName: String,
    val sourceInitial: String,
    val sourceWebsite: String,
    val author: String,
    val publishedTimeAgo: String,
    val readTimeMinutes: Int,
    val imageUrl: String?,
    val imageDrawableRes: Int?,
    val tagsRaw: String, // comma separated
    val savedAtTimestamp: Long = System.currentTimeMillis(),
    val personalNotes: String = ""
)
