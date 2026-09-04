package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * On-Screen Hebrew Virtual Keyboard helper component.
 * Allows easy, instant Hebrew typing directly in the simulation environment.
 */
@Composable
fun HebrewKeyboard(
    onCharTyped: (Char) -> Unit,
    onBackspace: () -> Unit,
    onSpace: () -> Unit,
    onClear: () -> Unit,
    onQuickSuggestion: (String) -> Unit = {},
    suggestions: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    var showNumbers by remember { mutableStateOf(false) }

    val row1Hebrew = listOf('ק', 'ר', 'א', 'ט', 'ו', 'ן', 'ם', 'פ')
    val row2Hebrew = listOf('ש', 'ד', 'ג', 'כ', 'ע', 'י', 'ח', 'ל', 'ך', 'ף')
    val row3Hebrew = listOf('ז', 'ס', 'ב', 'ה', 'נ', 'מ', 'צ', 'ת', 'ץ')

    val row1Numbers = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')
    val row2Symbols = listOf('!', '?', '.', ',', '-', ':', '(', ')', '"', '\'')

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp))
            .testTag("hebrew_virtual_keyboard"),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0F172A), // Dark slate navy
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header / Quick Suggestions row if provided
            if (suggestions.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    suggestions.forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF334155),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onQuickSuggestion(suggestion) }
                        ) {
                            Text(
                                text = suggestion,
                                color = Color(0xFF93C5FD),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            if (!showNumbers) {
                // Row 1 Letters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    row1Hebrew.forEach { char ->
                        HebrewKey(char = char, onClick = { onCharTyped(char) })
                    }
                }

                // Row 2 Letters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    row2Hebrew.forEach { char ->
                        HebrewKey(char = char, onClick = { onCharTyped(char) })
                    }
                }

                // Row 3 Letters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    row3Hebrew.forEach { char ->
                        HebrewKey(char = char, onClick = { onCharTyped(char) })
                    }
                }
            } else {
                // Numbers & Symbols Rows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    row1Numbers.forEach { char ->
                        HebrewKey(char = char, onClick = { onCharTyped(char) })
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    row2Symbols.forEach { char ->
                        HebrewKey(char = char, onClick = { onCharTyped(char) })
                    }
                }
            }

            // Bottom Action Controls: Mode Switch, Spacebar, Backspace, Clear
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Toggle Numbers / Letters
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (showNumbers) Color(0xFF6366F1) else Color(0xFF1E293B),
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showNumbers = !showNumbers }
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showNumbers) "אבג" else "123",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                // Spacebar
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E293B),
                    modifier = Modifier
                        .height(42.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSpace() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SpaceBar,
                            contentDescription = "Space",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "רווח",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Backspace
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF334155),
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onBackspace() }
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Backspace,
                            contentDescription = "Backspace",
                            tint = Color(0xFFFCA5A5),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Clear
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF450A0A),
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onClear() }
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "נקה",
                            color = Color(0xFFF87171),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HebrewKey(
    char: Char,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1E293B),
        modifier = Modifier
            .size(width = 34.dp, height = 40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(Color(0xFF1E293B))
        ) {
            Text(
                text = char.toString(),
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
