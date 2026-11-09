package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedArticleDao {
    @Query("SELECT * FROM saved_articles ORDER BY savedAtTimestamp DESC")
    fun getAllSaved(): Flow<List<SavedArticleEntity>>

    @Query("SELECT * FROM saved_articles WHERE id = :id LIMIT 1")
    fun getSavedById(id: String): Flow<SavedArticleEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: SavedArticleEntity)

    @Query("DELETE FROM saved_articles WHERE id = :id")
    suspend fun deleteArticleById(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE id = :id)")
    suspend fun isArticleSaved(id: String): Boolean

    @Query("SELECT COUNT(*) FROM saved_articles")
    fun getSavedCountFlow(): Flow<Int>
}
