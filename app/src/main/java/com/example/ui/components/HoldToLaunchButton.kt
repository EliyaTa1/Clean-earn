package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Giant prominent green Launch Screen-Time Button with anti-accidental click protection (2-second hold).
 */
@Composable
fun HoldToLaunchButton(
    balanceMinutes: Int,
    onLaunch: () -> Unit,
    onHoldTick: (step: Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val holdProgress = remember { Animatable(0f) }
    var isPressing by remember { mutableStateOf(false) }
    var holdJob by remember { mutableStateOf<Job?>(null) }

    val isEnabled = balanceMinutes > 0
    val scale = if (isPressing && isEnabled) 0.95f else 1.0f

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .scale(scale)
            .shadow(if (isEnabled) 12.dp else 2.dp, RoundedCornerShape(26.dp))
            .testTag("hold_to_launch_button"),
        shape = RoundedCornerShape(26.dp),
        color = if (isEnabled) Color(0xFF10B981) else Color(0xFF94A3B8) // Bright Green or Inactive Slate
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isEnabled) {
                        Brush.horizontalGradient(
                            listOf(Color(0xFF059669), Color(0xFF10B981), Color(0xFF34D399))
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(Color(0xFF64748B), Color(0xFF94A3B8))
                        )
                    }
                )
                .pointerInput(isEnabled) {
                    if (!isEnabled) return@pointerInput
                    detectTapGestures(
                        onPress = {
                            isPressing = true
                            var didLaunch = false
                            holdJob = coroutineScope.launch {
                                val durationMs = 2000
                                val steps = 10
                                val stepTime = durationMs / steps

                                launch {
                                    holdProgress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(durationMillis = durationMs, easing = LinearEasing)
                                    )
                                }

                                for (i in 1..steps) {
                                    delay(stepTime.toLong())
                                    onHoldTick(i)
                                }

                                // 2 seconds completed successfully!
                                didLaunch = true
                                isPressing = false
                                holdProgress.snapTo(0f)
                                onLaunch()
                            }

                            tryAwaitRelease()
                            if (!didLaunch) {
                                isPressing = false
                                holdJob?.cancel()
                                coroutineScope.launch {
                                    holdProgress.animateTo(0f, tween(150))
                                }
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Charging Progress Fill & Animated hold border ring
            Canvas(modifier = Modifier.fillMaxSize()) {
                val progress = holdProgress.value
                if (progress > 0f) {
                    // Charging progress background overlay
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFBBF24).copy(alpha = 0.5f),
                                Color(0xFFFDE047).copy(alpha = 0.7f)
                            )
                        ),
                        topLeft = Offset.Zero,
                        size = Size(size.width * progress, size.height),
                        cornerRadius = CornerRadius(26.dp.toPx(), 26.dp.toPx())
                    )

                    // Glowing border
                    val strokeW = 6.dp.toPx()
                    drawRoundRect(
                        color = Color(0xFFFACC15), // Glowing Golden Yellow
                        topLeft = Offset(strokeW / 2, strokeW / 2),
                        size = Size(size.width - strokeW, size.height - strokeW),
                        cornerRadius = CornerRadius(26.dp.toPx(), 26.dp.toPx()),
                        style = Stroke(width = strokeW, cap = StrokeCap.Round)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                // Giant Tablet / Screen Icon + Clock
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TabletAndroid,
                            contentDescription = "Screen",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Clock",
                            tint = Color(0xFFFDE047),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = if (isEnabled) {
                            if (isPressing) "להחזיק עוד רגע..." else "התחל זמן מסך ▶"
                        } else {
                            "סדרו כדי להרוויח דקות"
                        },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = if (isEnabled) "לחיצה רצופה של 2 שניות" else "הבנק ריק כרגע",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xCCFFFFFF)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Play triangle
                if (isEnabled) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEF08A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
