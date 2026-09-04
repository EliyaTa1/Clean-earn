package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildEntity
import com.example.ui.components.ChildAvatarView
import com.example.ui.components.SleepRestingAnimation
import com.example.ui.components.VisualCountdownGraphic
import java.util.Locale

@Composable
fun ScreenTimeCountdownScreen(
    child: ChildEntity,
    totalSeconds: Int,
    remainingSeconds: Int,
    isTimerRunning: Boolean,
    isTimeOver: Boolean,
    countdownVisualTheme: String,
    onTogglePauseResume: () -> Unit,
    onStopAndReturn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingFraction = if (totalSeconds > 0) {
        (remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTime = String.format(Locale.US, "%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    if (isTimeOver) {
                        listOf(Color(0xFF0F172A), Color(0xFF020617)) // Night bedtime sky
                    } else {
                        listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
                    }
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar: Child Avatar & Stop / Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChildAvatarView(
                        avatarKey = child.avatarKey,
                        modifier = Modifier.size(50.dp),
                        backgroundColor = Color(child.colorHex).copy(alpha = 0.2f),
                        borderColor = Color(child.colorHex)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = child.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(onClick = onStopAndReturn)
                        .testTag("stop_screen_time_button"),
                    color = Color(0xFF334155),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isTimeOver) Icons.Default.Check else Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = if (isTimeOver) Color(0xFF34D399) else Color(0xFFF87171),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isTimeOver) "סיום" else "שמור ועצור",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Middle Visual Area
            if (isTimeOver) {
                // Time's Up Screen State: Cute sleeping animation & friendly return message
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    SleepRestingAnimation(modifier = Modifier.size(240.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "הזמן נגמר! המסך נח 💤",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFDE047),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "בואו נסדר שוב בבית כדי לצבור עוד דקות!",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Active Countdown Visual: The Emptying Graphic (Ice cream cone / Battery / Star jar)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    VisualCountdownGraphic(
                        theme = countdownVisualTheme,
                        remainingFraction = remainingFraction,
                        modifier = Modifier.size(240.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Digital Clock Timer Display (MM:SS)
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Surface(
                            shape = RoundedCornerShape(22.dp),
                            color = Color(0xFF0F172A),
                            shadowElevation = 8.dp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = formattedTime,
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = if (minutes == 0) Color(0xFFF87171) else Color(0xFF38BDF8),
                                modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Action Controls
            if (isTimeOver) {
                Button(
                    onClick = onStopAndReturn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .shadow(10.dp, RoundedCornerShape(22.dp))
                        .testTag("back_to_tidy_button"),
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CleaningServices,
                            contentDescription = "Tidy",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "הולכים לסדר ולהרוויח! ✨",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Big Pause / Resume Button
                    Surface(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onTogglePauseResume)
                            .shadow(8.dp, CircleShape)
                            .testTag("pause_resume_button"),
                        color = if (isTimerRunning) Color(0xFFF59E0B) else Color(0xFF10B981),
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isTimerRunning) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(42.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
