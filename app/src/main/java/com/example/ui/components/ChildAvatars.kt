package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChildAvatarView(
    avatarKey: String,
    modifier: Modifier = Modifier.size(72.dp),
    backgroundColor: Color = Color(0xFFE0E7FF),
    borderColor: Color = Color(0xFF6366F1),
    borderWidth: Dp = 3.dp
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (avatarKey.lowercase()) {
                "teddy" -> drawAvatarTeddy(w, h)
                "bunny" -> drawAvatarBunny(w, h)
                "lion" -> drawAvatarLion(w, h)
                "cat" -> drawAvatarCat(w, h)
                "puppy" -> drawAvatarPuppy(w, h)
                "rocket" -> drawAvatarRocket(w, h)
                "dino" -> drawAvatarDino(w, h)
                else -> drawAvatarStar(w, h)
            }
        }
    }
}

private fun DrawScope.drawAvatarTeddy(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.52f

    // Ears
    drawCircle(Color(0xFFB45309), radius = w * 0.16f, center = Offset(cx - w * 0.26f, cy - h * 0.25f))
    drawCircle(Color(0xFFFDE68A), radius = w * 0.08f, center = Offset(cx - w * 0.26f, cy - h * 0.25f))
    drawCircle(Color(0xFFB45309), radius = w * 0.16f, center = Offset(cx + w * 0.26f, cy - h * 0.25f))
    drawCircle(Color(0xFFFDE68A), radius = w * 0.08f, center = Offset(cx + w * 0.26f, cy - h * 0.25f))

    // Head
    drawCircle(Color(0xFFD97706), radius = w * 0.36f, center = Offset(cx, cy))

    // Snout
    drawCircle(Color(0xFFFEF3C7), radius = w * 0.16f, center = Offset(cx, cy + h * 0.06f))
    // Nose
    drawCircle(Color(0xFF451A03), radius = w * 0.06f, center = Offset(cx, cy + h * 0.02f))
    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx - w * 0.12f, cy - h * 0.08f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx + w * 0.12f, cy - h * 0.08f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx - w * 0.10f, cy - h * 0.09f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx + w * 0.14f, cy - h * 0.09f))

    // Cheeks
    drawCircle(Color(0xFFFCA5A5), radius = w * 0.05f, center = Offset(cx - w * 0.22f, cy + h * 0.06f))
    drawCircle(Color(0xFFFCA5A5), radius = w * 0.05f, center = Offset(cx + w * 0.22f, cy + h * 0.06f))
}

private fun DrawScope.drawAvatarBunny(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.55f

    // Long ears
    drawOval(Color(0xFFEC4899), topLeft = Offset(cx - w * 0.24f, h * 0.06f), size = Size(w * 0.18f, h * 0.44f))
    drawOval(Color(0xFFFBCFE8), topLeft = Offset(cx - w * 0.20f, h * 0.12f), size = Size(w * 0.10f, h * 0.32f))
    drawOval(Color(0xFFEC4899), topLeft = Offset(cx + w * 0.06f, h * 0.06f), size = Size(w * 0.18f, h * 0.44f))
    drawOval(Color(0xFFFBCFE8), topLeft = Offset(cx + w * 0.10f, h * 0.12f), size = Size(w * 0.10f, h * 0.32f))

    // Head
    drawCircle(Color(0xFFFCE7F3), radius = w * 0.35f, center = Offset(cx, cy))

    // Eyes
    drawCircle(Color(0xFF831843), radius = w * 0.045f, center = Offset(cx - w * 0.12f, cy - h * 0.04f))
    drawCircle(Color(0xFF831843), radius = w * 0.045f, center = Offset(cx + w * 0.12f, cy - h * 0.04f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx - w * 0.10f, cy - h * 0.05f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx + w * 0.14f, cy - h * 0.05f))

    // Nose
    drawCircle(Color(0xFFDB2777), radius = w * 0.04f, center = Offset(cx, cy + h * 0.06f))
    // Cheeks
    drawCircle(Color(0xFFF472B6), radius = w * 0.06f, center = Offset(cx - w * 0.20f, cy + h * 0.08f))
    drawCircle(Color(0xFFF472B6), radius = w * 0.06f, center = Offset(cx + w * 0.20f, cy + h * 0.08f))
}

private fun DrawScope.drawAvatarLion(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.50f

    // Lion Mane
    drawCircle(Color(0xFFEA580C), radius = w * 0.44f, center = Offset(cx, cy))

    // Head
    drawCircle(Color(0xFFFDE047), radius = w * 0.30f, center = Offset(cx, cy))

    // Ears
    drawCircle(Color(0xFFF59E0B), radius = w * 0.09f, center = Offset(cx - w * 0.22f, cy - h * 0.20f))
    drawCircle(Color(0xFFF59E0B), radius = w * 0.09f, center = Offset(cx + w * 0.22f, cy - h * 0.20f))

    // Snout
    drawCircle(Color(0xFFFEF3C7), radius = w * 0.14f, center = Offset(cx, cy + h * 0.06f))
    drawCircle(Color(0xFF78350F), radius = w * 0.05f, center = Offset(cx, cy + h * 0.03f))

    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(cx - w * 0.10f, cy - h * 0.06f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.04f, center = Offset(cx + w * 0.10f, cy - h * 0.06f))
}

private fun DrawScope.drawAvatarCat(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.52f

    // Pointy Ears
    val leftEar = Path().apply {
        moveTo(cx - w * 0.32f, cy - h * 0.10f)
        lineTo(cx - w * 0.25f, cy - h * 0.42f)
        lineTo(cx - w * 0.05f, cy - h * 0.25f)
        close()
    }
    drawPath(leftEar, Color(0xFF6366F1))
    val rightEar = Path().apply {
        moveTo(cx + w * 0.32f, cy - h * 0.10f)
        lineTo(cx + w * 0.25f, cy - h * 0.42f)
        lineTo(cx + w * 0.05f, cy - h * 0.25f)
        close()
    }
    drawPath(rightEar, Color(0xFF6366F1))

    // Head
    drawCircle(Color(0xFFA5B4FC), radius = w * 0.34f, center = Offset(cx, cy))

    // Eyes
    drawCircle(Color(0xFF1E1B4B), radius = w * 0.045f, center = Offset(cx - w * 0.11f, cy - h * 0.04f))
    drawCircle(Color(0xFF1E1B4B), radius = w * 0.045f, center = Offset(cx + w * 0.11f, cy - h * 0.04f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx - w * 0.09f, cy - h * 0.05f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.015f, center = Offset(cx + w * 0.13f, cy - h * 0.05f))

    // Nose & Whiskers
    drawCircle(Color(0xFFEC4899), radius = w * 0.035f, center = Offset(cx, cy + h * 0.05f))
    drawLine(Color(0xFF4338CA), Offset(cx - w * 0.10f, cy + h * 0.06f), Offset(cx - w * 0.28f, cy + h * 0.03f), strokeWidth = w * 0.02f, cap = StrokeCap.Round)
    drawLine(Color(0xFF4338CA), Offset(cx + w * 0.10f, cy + h * 0.06f), Offset(cx + w * 0.28f, cy + h * 0.03f), strokeWidth = w * 0.02f, cap = StrokeCap.Round)
}

private fun DrawScope.drawAvatarPuppy(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.48f

    // Droopy Ears
    drawOval(Color(0xFF92400E), topLeft = Offset(cx - w * 0.42f, cy - h * 0.20f), size = Size(w * 0.22f, h * 0.42f))
    drawOval(Color(0xFF92400E), topLeft = Offset(cx + w * 0.20f, cy - h * 0.20f), size = Size(w * 0.22f, h * 0.42f))

    // Head
    drawCircle(Color(0xFFFDE68A), radius = w * 0.33f, center = Offset(cx, cy))

    // Snout
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.15f, center = Offset(cx, cy + h * 0.10f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.055f, center = Offset(cx, cy + h * 0.06f))

    // Tongue
    drawOval(Color(0xFFF43F5E), topLeft = Offset(cx - w * 0.05f, cy + h * 0.16f), size = Size(w * 0.10f, h * 0.14f))

    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx - w * 0.11f, cy - h * 0.06f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx + w * 0.11f, cy - h * 0.06f))
}

private fun DrawScope.drawAvatarRocket(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.50f

    // Flames
    val flame = Path().apply {
        moveTo(cx - w * 0.12f, cy + h * 0.25f)
        lineTo(cx, cy + h * 0.44f)
        lineTo(cx + w * 0.12f, cy + h * 0.25f)
        close()
    }
    drawPath(flame, Color(0xFFEF4444))
    val flameInner = Path().apply {
        moveTo(cx - w * 0.06f, cy + h * 0.25f)
        lineTo(cx, cy + h * 0.36f)
        lineTo(cx + w * 0.06f, cy + h * 0.25f)
        close()
    }
    drawPath(flameInner, Color(0xFFFBBF24))

    // Fins
    val leftFin = Path().apply {
        moveTo(cx - w * 0.16f, cy + h * 0.10f)
        lineTo(cx - w * 0.34f, cy + h * 0.26f)
        lineTo(cx - w * 0.14f, cy + h * 0.26f)
        close()
    }
    drawPath(leftFin, Color(0xFF0284C7))
    val rightFin = Path().apply {
        moveTo(cx + w * 0.16f, cy + h * 0.10f)
        lineTo(cx + w * 0.34f, cy + h * 0.26f)
        lineTo(cx + w * 0.14f, cy + h * 0.26f)
        close()
    }
    drawPath(rightFin, Color(0xFF0284C7))

    // Rocket Body
    val body = Path().apply {
        moveTo(cx, cy - h * 0.38f)
        cubicTo(cx + w * 0.22f, cy - h * 0.15f, cx + w * 0.18f, cy + h * 0.25f, cx + w * 0.16f, cy + h * 0.25f)
        lineTo(cx - w * 0.16f, cy + h * 0.25f)
        cubicTo(cx - w * 0.18f, cy + h * 0.25f, cx - w * 0.22f, cy - h * 0.15f, cx, cy - h * 0.38f)
        close()
    }
    drawPath(body, Color(0xFFF8FAFC))

    // Porthole Window
    drawCircle(Color(0xFF38BDF8), radius = w * 0.08f, center = Offset(cx, cy - h * 0.05f))
    drawCircle(Color(0xFF0284C7), radius = w * 0.08f, center = Offset(cx, cy - h * 0.05f), style = Stroke(width = w * 0.02f))
}

private fun DrawScope.drawAvatarDino(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.52f

    // Dino Head
    drawCircle(Color(0xFF22C55E), radius = w * 0.34f, center = Offset(cx, cy))

    // Spikes on head
    for (i in -2..2) {
        val angle = i * 25.0
        val sx = cx + (w * 0.34f * Math.sin(Math.toRadians(angle))).toFloat()
        val sy = cy - (h * 0.34f * Math.cos(Math.toRadians(angle))).toFloat()
        drawCircle(Color(0xFFF59E0B), radius = w * 0.06f, center = Offset(sx, sy))
    }

    // Snout
    drawRoundRect(
        color = Color(0xFF16A34A),
        topLeft = Offset(cx - w * 0.20f, cy),
        size = Size(w * 0.40f, h * 0.22f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f, w * 0.06f)
    )
    drawCircle(Color(0xFF14532D), radius = w * 0.035f, center = Offset(cx - w * 0.08f, cy + h * 0.08f))
    drawCircle(Color(0xFF14532D), radius = w * 0.035f, center = Offset(cx + w * 0.08f, cy + h * 0.08f))

    // Big Cartoon Eyes
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.08f, center = Offset(cx - w * 0.12f, cy - h * 0.12f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.08f, center = Offset(cx + w * 0.12f, cy - h * 0.12f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx - w * 0.10f, cy - h * 0.12f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.045f, center = Offset(cx + w * 0.10f, cy - h * 0.12f))
}

private fun DrawScope.drawAvatarStar(w: Float, h: Float) {
    val cx = w * 0.5f
    val cy = h * 0.50f
    val radius = w * 0.38f

    // Star path
    val path = Path()
    val points = 5
    val innerRadius = radius * 0.48f

    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val angle = i * Math.PI / points - Math.PI / 2.0
        val x = (cx + r * Math.cos(angle)).toFloat()
        val y = (cy + r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()

    drawPath(path, Brush.radialGradient(listOf(Color(0xFFFDE047), Color(0xFFF59E0B))))

    // Star face
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(cx - w * 0.09f, cy - h * 0.02f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.035f, center = Offset(cx + w * 0.09f, cy - h * 0.02f))
    drawCircle(Color(0xFFF43F5E), radius = w * 0.045f, center = Offset(cx - w * 0.15f, cy + h * 0.06f))
    drawCircle(Color(0xFFF43F5E), radius = w * 0.045f, center = Offset(cx + w * 0.15f, cy + h * 0.06f))
}
