package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.NewsArticle
import com.example.model.ReaderFontType
import com.example.model.ReaderSettings
import com.example.model.ReaderTheme
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: NewsArticle,
    isSaved: Boolean,
    isTtsPlaying: Boolean,
    settings: ReaderSettings,
    onBack: () -> Unit,
    onToggleSave: () -> Unit,
    onToggleTts: () -> Unit,
    onOpenReaderSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isSystemDark = isSystemInDarkTheme()

    // Determine reader surface background & text colors based on selected ReaderTheme
    val readerBg = when (settings.theme) {
        ReaderTheme.DEFAULT -> if (isSystemDark) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.background
        ReaderTheme.PAPER -> Color(if (isSystemDark) settings.theme.bgDark else settings.theme.bgLight)
        ReaderTheme.SEPIA -> Color(if (isSystemDark) settings.theme.bgDark else settings.theme.bgLight)
        ReaderTheme.MIDNIGHT -> Color(settings.theme.bgDark)
        ReaderTheme.OLED -> Color(settings.theme.bgDark)
    }

    val readerTextColor = when (settings.theme) {
        ReaderTheme.DEFAULT -> if (isSystemDark) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground
        ReaderTheme.PAPER -> Color(if (isSystemDark) settings.theme.textDark else settings.theme.textLight)
        ReaderTheme.SEPIA -> Color(if (isSystemDark) settings.theme.textDark else settings.theme.textLight)
        ReaderTheme.MIDNIGHT -> Color(settings.theme.textDark)
        ReaderTheme.OLED -> Color(settings.theme.textDark)
    }

    val readerFontFamily = when (settings.fontType) {
        ReaderFontType.SERIF -> FontFamily.Serif
        ReaderFontType.SANS_SERIF -> FontFamily.SansSerif
        ReaderFontType.MONOSPACE -> FontFamily.Monospace
    }

    val fontSize = settings.fontSizeSp.sp
    val lineHeight = (settings.fontSizeSp * settings.lineSpacingMultiplier).sp

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("article_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = readerTextColor
                        )
                    }
                },
                actions = {
                    // Audio Listen Button
                    IconButton(
                        onClick = onToggleTts,
                        modifier = Modifier.testTag("article_listen_button")
                    ) {
                        Icon(
                            imageVector = if (isTtsPlaying) Icons.Default.Pause else Icons.Default.Headphones,
                            contentDescription = if (isTtsPlaying) "Pause Audio" else "Listen",
                            tint = if (isTtsPlaying) AccentAmber else readerTextColor
                        )
                    }

                    // Reader Typography Settings Button
                    IconButton(
                        onClick = onOpenReaderSettings,
                        modifier = Modifier.testTag("article_reader_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "Reader Typography Settings",
                            tint = readerTextColor
                        )
                    }

                    // Bookmark Button
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.testTag("article_save_button")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isSaved) "Saved" else "Save",
                            tint = if (isSaved) AccentAmber else readerTextColor
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, article.title)
                                putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.summary}\n\nRead more on Briefly.")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share News Story"))
                        },
                        modifier = Modifier.testTag("article_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = readerTextColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = readerBg
                )
            )
        },
        containerColor = readerBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(bottom = 96.dp)
        ) {
            // Category & Meta Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Surface(
                    color = Color(article.category.chipColor),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = article.category.displayName.uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(
                    text = "${article.publishedTimeAgo} • ${article.readTimeMinutes} min read",
                    fontSize = 12.sp,
                    color = readerTextColor.copy(alpha = 0.65f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Headline
            Text(
                text = article.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = readerFontFamily,
                color = readerTextColor,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Author & Source Byline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AccentBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = article.source.initial,
                        color = AccentBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = article.author,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = readerTextColor
                    )
                    Text(
                        text = "${article.source.name} • Verified Wire",
                        fontSize = 11.sp,
                        color = readerTextColor.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero banner image (if present)
            if (article.imageDrawableRes != null) {
                Image(
                    painter = painterResource(id = article.imageDrawableRes),
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Key Takeaways Highlight Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AccentAmber.copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AccentAmber.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Key Takeaways",
                            tint = AccentAmber,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Key Takeaways",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = readerTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    article.keyTakeaways.forEach { takeaway ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = AccentAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = takeaway,
                                color = readerTextColor.copy(alpha = 0.9f),
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = readerTextColor.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(18.dp))

            // Body Paragraphs
            article.content.forEachIndexed { index, paragraph ->
                Text(
                    text = paragraph,
                    fontSize = fontSize,
                    fontFamily = readerFontFamily,
                    color = readerTextColor.copy(alpha = 0.92f),
                    lineHeight = lineHeight,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tags row
            if (article.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    article.tags.forEach { tag ->
                        Surface(
                            color = readerTextColor.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                color = AccentBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom Action Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onToggleTts,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("article_bottom_listen_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isTtsPlaying) Icons.Default.Pause else Icons.Default.Headphones,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isTtsPlaying) "Pause" else "Listen")
                }

                OutlinedButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("article_bottom_save_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = if (isSaved) AccentAmber else readerTextColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isSaved) "Saved" else "Save Offline",
                        color = readerTextColor
                    )
                }
            }
        }
    }
}
