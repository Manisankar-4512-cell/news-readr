package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.TtsPlaybackState
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.EditorialNavy

@Composable
fun AudioPlayerBar(
    ttsState: TtsPlaybackState,
    onTogglePlayPause: () -> Unit,
    onNextSection: () -> Unit,
    onPrevSection: () -> Unit,
    onCycleSpeed: () -> Unit,
    onClose: () -> Unit,
    onClickBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val article = ttsState.currentArticle

    AnimatedVisibility(
        visible = article != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier
    ) {
        if (article != null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onClickBar)
                    .testTag("audio_player_bar"),
                color = EditorialNavy,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Progress Indicator across sections
                    val progress = if (ttsState.totalSections > 0) {
                        (ttsState.currentSectionIndex + 1).toFloat() / ttsState.totalSections.toFloat()
                    } else {
                        0f
                    }
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp),
                        color = AccentAmber,
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title & section info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(if (ttsState.isPlaying) AccentAmber else Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (ttsState.isPlaying) "LISTENING • Section ${ttsState.currentSectionIndex + 1}/${ttsState.totalSections}" else "PAUSED",
                                    color = AccentAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = article.title,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Playback Controls
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Speed Chip
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable(onClick = onCycleSpeed)
                                    .testTag("tts_speed_button"),
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${ttsState.speechRate}x",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = onPrevSection,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("tts_prev_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Previous Section",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onTogglePlayPause,
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AccentBlue, CircleShape)
                                    .testTag("tts_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (ttsState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (ttsState.isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onNextSection,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("tts_next_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Next Section",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("tts_close_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close Audio",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
