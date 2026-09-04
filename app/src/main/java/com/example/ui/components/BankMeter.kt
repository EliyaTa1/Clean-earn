package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

/**
 * Visual Bank Meter showing current screen-time balance and today's accumulated progress.
 * Zero-text needed for kids: features animated rising water/liquid, golden coins, and glowing stars!
 */
@Composable
fun VisualBankMeter(
    currentBalanceMinutes: Int,
    todayEarnedMinutes: Int,
    dailyCapMinutes: Int,
    modifier: Modifier = Modifier
) {
    val fillFraction = (todayEarnedMinutes.toFloat() / dailyCapMinutes.coerceAtLeast(1).toFloat())
        .coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fillFraction,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "bank_fill"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "wave_anim")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .testTag("bank_meter_card"),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1E1B4B) // Rich Midnight Navy
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left / Main: The Filling Liquid / Battery Tank
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
            ) {
                // Animated Water/Energy wave filling up
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    val waterHeight = h * animatedFraction
                    val baseWaterY = h - waterHeight

                    // Wave path
                    val wavePath = Path().apply {
                        moveTo(0f, h)
                        lineTo(0f, baseWaterY)
                        val waveSegments = 20
                        val step = w / waveSegments
                        for (i in 0..waveSegments) {
                            val x = i * step
                            val y = if (animatedFraction > 0.01f) {
                                baseWaterY + (sin(waveOffset + (i * 0.45f)) * 4f).toFloat()
                            } else {
                                h
                            }
                            lineTo(x, y)
                        }
                        lineTo(w, h)
                        close()
                    }

                    val gradient = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF38BDF8), // Bright Cyan
                            Color(0xFF0284C7), // Deep Ocean Blue
                            Color(0xFF4F46E5)  // Indigo
                        ),
                        startY = baseWaterY,
                        endY = h
                    )
                    drawPath(wavePath, gradient)

                    // Bubble particles
                    if (animatedFraction > 0.1f) {
                        for (i in 1..4) {
                            val bx = (w * (i * 0.22f + (waveOffset * 0.05f) % 0.1f)) % w
                            val by = h - (waterHeight * (0.2f + (i * 0.18f)))
                            drawCircle(Color(0x88FFFFFF), radius = 3.5f, center = Offset(bx, by))
                        }
                    }

                    // Border outline
                    drawRoundRect(
                        color = Color(0x33FFFFFF),
                        size = size,
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }

                // Foreground content over tank: Big Shiny Golden Clock & Coins
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Golden Animated Coin
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFFFDE047), Color(0xFFEAB308), Color(0xFFCA8A04))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "$currentBalanceMinutes",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "דקות",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF93C5FD)
                        )
                    }

                    // Today's Cap indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33FFFFFF))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$todayEarnedMinutes / $dailyCapMinutes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (todayEarnedMinutes >= dailyCapMinutes) Color(0xFF34D399) else Color(0xFFCBD5E1)
                        )
                    }
                }
            }
        }
    }
}
