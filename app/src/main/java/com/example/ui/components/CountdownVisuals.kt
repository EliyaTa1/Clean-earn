package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Visual Countdown Emptying Graphics:
 * 1. Giant Delicious Melting / Eaten Ice Cream Cone
 * 2. High-Tech Discharging Energy Battery
 * 3. Glowing Star Jar
 */
@Composable
fun VisualCountdownGraphic(
    theme: String, // "ICE_CREAM", "BATTERY", "STAR_JAR"
    remainingFraction: Float, // 1.0f down to 0.0f
    modifier: Modifier = Modifier.size(240.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "countdown_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (theme) {
                "BATTERY" -> drawBatteryGraphic(w, h, remainingFraction, pulse)
                "STAR_JAR" -> drawStarJarGraphic(w, h, remainingFraction, pulse)
                else -> drawIceCreamGraphic(w, h, remainingFraction, pulse)
            }
        }
    }
}

private fun DrawScope.drawIceCreamGraphic(
    w: Float,
    h: Float,
    remainingFraction: Float,
    pulse: Float
) {
    // Waffle Cone (Bottom)
    val conePath = Path().apply {
        moveTo(w * 0.28f, h * 0.58f)
        lineTo(w * 0.72f, h * 0.58f)
        lineTo(w * 0.50f, h * 0.95f)
        close()
    }
    val coneBrush = Brush.verticalGradient(
        listOf(Color(0xFFD97706), Color(0xFF92400E))
    )
    drawPath(conePath, coneBrush)

    // Waffle grid lines
    drawLine(Color(0x55FFFFFF), Offset(w * 0.35f, h * 0.65f), Offset(w * 0.60f, h * 0.85f), strokeWidth = 3f)
    drawLine(Color(0x55FFFFFF), Offset(w * 0.42f, h * 0.60f), Offset(w * 0.55f, h * 0.90f), strokeWidth = 3f)
    drawLine(Color(0x55FFFFFF), Offset(w * 0.65f, h * 0.65f), Offset(w * 0.40f, h * 0.85f), strokeWidth = 3f)

    // 3 Scoops of Ice Cream that disappear / shrink from top to bottom as remainingFraction decreases
    // Scoop 1 (Bottom Scoop - Chocolate/Berry)
    val scoop1Progress = (remainingFraction / 0.33f).coerceIn(0f, 1f)
    if (scoop1Progress > 0.05f) {
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFF818CF8), Color(0xFF4F46E5))),
            radius = (w * 0.22f * scoop1Progress),
            center = Offset(w * 0.50f, h * 0.54f)
        )
    }

    // Scoop 2 (Middle Scoop - Strawberry Pink)
    val scoop2Progress = ((remainingFraction - 0.33f) / 0.33f).coerceIn(0f, 1f)
    if (scoop2Progress > 0.05f) {
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFFF472B6), Color(0xFFDB2777))),
            radius = (w * 0.20f * scoop2Progress),
            center = Offset(w * 0.50f, h * 0.38f)
        )
        // Sprinkles on middle scoop
        drawCircle(Color(0xFFFDE047), radius = 4f, center = Offset(w * 0.42f, h * 0.36f))
        drawCircle(Color(0xFF34D399), radius = 4f, center = Offset(w * 0.56f, h * 0.34f))
    }

    // Scoop 3 (Top Scoop - Vanilla/Mint with Cherry on top!)
    val scoop3Progress = ((remainingFraction - 0.66f) / 0.34f).coerceIn(0f, 1f)
    if (scoop3Progress > 0.05f) {
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFF6EE7B7), Color(0xFF059669))),
            radius = (w * 0.17f * scoop3Progress * pulse),
            center = Offset(w * 0.50f, h * 0.24f)
        )
        // Cherry on Top!
        drawCircle(Color(0xFFE11D48), radius = w * 0.055f * scoop3Progress, center = Offset(w * 0.50f, h * 0.12f))
        // Cherry stem
        val stem = Path().apply {
            moveTo(w * 0.50f, h * 0.10f)
            quadraticTo(w * 0.55f, h * 0.05f, w * 0.62f, h * 0.04f)
        }
        drawPath(stem, Color(0xFF15803D), style = Stroke(width = 3f, cap = StrokeCap.Round))
    }
}

private fun DrawScope.drawBatteryGraphic(
    w: Float,
    h: Float,
    remainingFraction: Float,
    pulse: Float
) {
    // Battery Terminal Top
    drawRoundRect(
        color = Color(0xFF94A3B8),
        topLeft = Offset(w * 0.40f, h * 0.08f),
        size = Size(w * 0.20f, h * 0.08f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Outer Battery Body
    drawRoundRect(
        color = Color(0xFF1E293B),
        topLeft = Offset(w * 0.22f, h * 0.15f),
        size = Size(w * 0.56f, h * 0.75f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
    )
    drawRoundRect(
        color = Color(0xFF475569),
        topLeft = Offset(w * 0.22f, h * 0.15f),
        size = Size(w * 0.56f, h * 0.75f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f),
        style = Stroke(width = 4.dp.toPx())
    )

    // Inner Energy Fill
    val innerH = h * 0.67f
    val fillH = innerH * remainingFraction
    val fillTopY = (h * 0.15f + innerH) - fillH

    val energyColor = when {
        remainingFraction > 0.5f -> Color(0xFF10B981) // Emerald Green
        remainingFraction > 0.2f -> Color(0xFFF59E0B) // Amber
        else -> Color(0xFFEF4444) // Red
    }

    if (remainingFraction > 0.02f) {
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(energyColor.copy(alpha = 0.9f), energyColor)
            ),
            topLeft = Offset(w * 0.26f, fillTopY + h * 0.04f),
            size = Size(w * 0.48f, fillH),
            cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
        )

        // Lightning Bolt Symbol in Center
        val boltPath = Path().apply {
            moveTo(w * 0.52f, h * 0.38f)
            lineTo(w * 0.44f, h * 0.52f)
            lineTo(w * 0.51f, h * 0.52f)
            lineTo(w * 0.48f, h * 0.66f)
            lineTo(w * 0.58f, h * 0.48f)
            lineTo(w * 0.51f, h * 0.48f)
            close()
        }
        drawPath(boltPath, Color(0xFFFEF08A))
    }
}

private fun DrawScope.drawStarJarGraphic(
    w: Float,
    h: Float,
    remainingFraction: Float,
    pulse: Float
) {
    // Glass Jar Body
    drawRoundRect(
        color = Color(0x3338BDF8),
        topLeft = Offset(w * 0.20f, h * 0.22f),
        size = Size(w * 0.60f, h * 0.68f),
        cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
    )
    drawRoundRect(
        color = Color(0xFF38BDF8),
        topLeft = Offset(w * 0.20f, h * 0.22f),
        size = Size(w * 0.60f, h * 0.68f),
        cornerRadius = CornerRadius(w * 0.12f, w * 0.12f),
        style = Stroke(width = 3.dp.toPx())
    )

    // Jar Cork Lid
    drawRoundRect(
        color = Color(0xFFB45309),
        topLeft = Offset(w * 0.28f, h * 0.14f),
        size = Size(w * 0.44f, h * 0.10f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    // Stars inside jar (number of stars depends on remainingFraction)
    val starCount = (remainingFraction * 8).toInt()
    val starPositions = listOf(
        Offset(w * 0.50f, h * 0.72f),
        Offset(w * 0.35f, h * 0.62f),
        Offset(w * 0.65f, h * 0.60f),
        Offset(w * 0.42f, h * 0.48f),
        Offset(w * 0.58f, h * 0.44f),
        Offset(w * 0.32f, h * 0.36f),
        Offset(w * 0.68f, h * 0.34f),
        Offset(w * 0.50f, h * 0.30f)
    )

    for (i in 0 until starCount.coerceAtMost(starPositions.size)) {
        val pos = starPositions[i]
        drawSparkle(pos.x, pos.y, w * 0.07f * pulse, Color(0xFFFDE047))
    }
}

/**
 * Friendly Bedtime / Resting Mascot Animation when time reaches 00:00!
 */
@Composable
fun SleepRestingAnimation(
    modifier: Modifier = Modifier.size(240.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sleep_anim")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    val zFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "z"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * 0.50f
            val cy = h * 0.55f

            // Night Sky Glow / Cloud
            drawCircle(Color(0x334338CA), radius = w * 0.45f, center = Offset(cx, cy))

            // Crescent Moon
            val moonCenter = Offset(w * 0.78f, h * 0.22f)
            drawCircle(Color(0xFFFDE047), radius = w * 0.12f, center = moonCenter)
            drawCircle(Color(0xFF0F172A), radius = w * 0.10f, center = Offset(moonCenter.x - w * 0.05f, moonCenter.y - h * 0.03f))

            // Cozy Bed Pillow
            drawRoundRect(
                color = Color(0xFFFFFFFF),
                topLeft = Offset(w * 0.20f, h * 0.35f),
                size = Size(w * 0.60f, h * 0.30f),
                cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
            )

            // Sleeping Teddy Head
            drawCircle(Color(0xFFD97706), radius = w * 0.26f * breathScale, center = Offset(cx, cy - h * 0.05f))

            // Sleeping Closed Eyes (Happy curved arcs)
            drawArc(
                color = Color(0xFF451A03),
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(cx - w * 0.16f, cy - h * 0.12f),
                size = Size(w * 0.10f, h * 0.08f),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFF451A03),
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(cx + w * 0.06f, cy - h * 0.12f),
                size = Size(w * 0.10f, h * 0.08f),
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // Cute Snout & Nose
            drawCircle(Color(0xFFFEF3C7), radius = w * 0.10f, center = Offset(cx, cy - h * 0.02f))
            drawCircle(Color(0xFF451A03), radius = w * 0.035f, center = Offset(cx, cy - h * 0.04f))

            // Nightcap on head (Blue starry cap)
            val capPath = Path().apply {
                moveTo(cx - w * 0.20f, cy - h * 0.22f)
                lineTo(cx + w * 0.20f, cy - h * 0.22f)
                lineTo(cx + w * 0.32f, cy - h * 0.44f)
                close()
            }
            drawPath(capPath, Color(0xFF3B82F6))
            drawCircle(Color(0xFFFFFFFF), radius = w * 0.05f, center = Offset(cx + w * 0.32f, cy - h * 0.44f))

            // Warm Cozy Blanket covering teddy
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color(0xFF818CF8), Color(0xFF4F46E5))),
                topLeft = Offset(w * 0.12f, cy + h * 0.05f),
                size = Size(w * 0.76f, h * 0.38f),
                cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
            )

            // Floating "Z z z" stars
            drawSparkle(w * 0.28f, (h * 0.24f) - zFloat, w * 0.05f, Color(0xFFFDE047))
            drawSparkle(w * 0.38f, (h * 0.16f) - (zFloat * 1.2f), w * 0.07f, Color(0xFFFDE047))
        }
    }
}
