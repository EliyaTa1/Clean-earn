package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val radius: Float,
    val color: Color
)

@Composable
fun ParticleOverlay(
    trigger: Long, // Pass a timestamp to trigger new burst
    modifier: Modifier = Modifier
) {
    if (trigger == 0L) return

    val progress = remember(trigger) { Animatable(0f) }
    val particles = remember(trigger) {
        val colors = listOf(
            Color(0xFFFBBF24), // Gold
            Color(0xFFF59E0B), // Amber
            Color(0xFF38BDF8), // Sky Blue
            Color(0xFF34D399), // Emerald
            Color(0xFFF472B6), // Pink
            Color(0xFFA78BFA)  // Purple
        )
        List(30) {
            val angle = Random.nextDouble(0.0, Math.PI * 2)
            val speed = Random.nextFloat() * 400f + 150f
            Particle(
                x = 0.5f,
                y = 0.5f,
                vx = (Math.cos(angle) * speed).toFloat(),
                vy = (Math.sin(angle) * speed).toFloat(),
                radius = Random.nextFloat() * 6f + 5f,
                color = colors.random()
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutLinearInEasing)
        )
    }

    if (progress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val t = progress.value

            particles.forEach { p ->
                val px = (w * p.x) + (p.vx * t)
                val py = (h * p.y) + (p.vy * t) + (200f * t * t) // gravity
                val alpha = (1f - t).coerceIn(0f, 1f)
                drawCircle(
                    color = p.color.copy(alpha = alpha),
                    radius = p.radius * (1f - t * 0.5f),
                    center = Offset(px, py)
                )
            }
        }
    }
}
