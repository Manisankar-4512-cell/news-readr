package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReaderFontType
import com.example.model.ReaderSettings
import com.example.model.ReaderTheme
import com.example.ui.theme.AccentBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsSheet(
    settings: ReaderSettings,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onFontSizeDelta: (Float) -> Unit,
    onFontTypeChange: (ReaderFontType) -> Unit,
    onThemeChange: (ReaderTheme) -> Unit,
    onLineSpacingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier.testTag("reader_settings_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Reader Preferences",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Personalize your typography, spacing and theme",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Theme Color Selector
            Text(
                text = "READING THEME",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReaderTheme.entries.forEach { themeOption ->
                    val isSelected = themeOption == settings.theme
                    val previewBg = Color(themeOption.bgLight)
                    val previewBorder = if (isSelected) AccentBlue else Color.LightGray.copy(alpha = 0.5f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onThemeChange(themeOption) }
                            .padding(4.dp)
                            .testTag("theme_chip_${themeOption.name}")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(previewBg)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = previewBorder,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = if (themeOption == ReaderTheme.OLED || themeOption == ReaderTheme.MIDNIGHT) Color.White else Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "Aa",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(themeOption.textLight)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = themeOption.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Font Size Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FONT SIZE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "${settings.fontSizeSp.toInt()} sp",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(100.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { onFontSizeDelta(-1.5f) },
                        modifier = Modifier.size(36.dp).testTag("font_decrease_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease Font Size",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "A",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    IconButton(
                        onClick = { onFontSizeDelta(1.5f) },
                        modifier = Modifier.size(36.dp).testTag("font_increase_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase Font Size",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Typeface Choice
            Text(
                text = "TYPEFACE",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReaderFontType.entries.forEach { fontOption ->
                    val isSelected = fontOption == settings.fontType
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFontTypeChange(fontOption) },
                        label = {
                            Text(
                                text = fontOption.displayName,
                                fontFamily = when (fontOption) {
                                    ReaderFontType.SERIF -> FontFamily.Serif
                                    ReaderFontType.SANS_SERIF -> FontFamily.SansSerif
                                    ReaderFontType.MONOSPACE -> FontFamily.Monospace
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("font_chip_${fontOption.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Line Spacing Multiplier
            Text(
                text = "LINE SPACING",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val spacings = listOf(
                    "Compact" to 1.25f,
                    "Balanced" to 1.45f,
                    "Relaxed" to 1.75f
                )
                spacings.forEach { (label, value) ->
                    val isSelected = (settings.lineSpacingMultiplier - value).let { kotlin.math.abs(it) < 0.05f }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onLineSpacingChange(value) },
                        label = { Text(label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("spacing_chip_${label.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
