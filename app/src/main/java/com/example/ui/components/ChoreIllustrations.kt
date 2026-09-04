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
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.model.ChoreIcon

@Composable
fun ChoreIllustration(
    icon: ChoreIcon,
    modifier: Modifier = Modifier.size(80.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "chore_anim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (icon) {
                ChoreIcon.TRASH_WRAPPER -> drawTrashWrapper(w, h, pulse)
                ChoreIcon.CUP_SINK -> drawCupSink(w, h, pulse)
                ChoreIcon.TEDDY_ONE -> drawTeddyOne(w, h, pulse)
                ChoreIcon.BIG_TRASH -> drawBigTrash(w, h, pulse)
                ChoreIcon.BED_TIDY -> drawBedTidy(w, h, pulse)
                ChoreIcon.SWEEP_ROOM -> drawSweepRoom(w, h, pulse)
                ChoreIcon.SOFA_PILLOWS -> drawSofaPillows(w, h, pulse)
                ChoreIcon.TEDDY_GROUP -> drawTeddyGroup(w, h, pulse)
                ChoreIcon.TOYS_BOX -> drawToysBox(w, h, pulse)
                ChoreIcon.SWEEP_LIVING -> drawSweepLiving(w, h, pulse)
                ChoreIcon.LIVING_ROOM_CLEAN -> drawLivingRoomClean(w, h, pulse)
                ChoreIcon.PLANT_WATER -> drawPlantWater(w, h, pulse)
                ChoreIcon.SHOES_RACK -> drawShoesRack(w, h, pulse)
                ChoreIcon.CUSTOM -> drawCustomChore(w, h, pulse)
            }
        }
    }
}

private fun DrawScope.drawTrashWrapper(w: Float, h: Float, pulse: Float) {
    // Trash bin body
    val binBrush = Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    val binPath = Path().apply {
        moveTo(w * 0.25f, h * 0.40f)
        lineTo(w * 0.75f, h * 0.40f)
        lineTo(w * 0.70f, h * 0.90f)
        lineTo(w * 0.30f, h * 0.90f)
        close()
    }
    drawPath(binPath, binBrush)

    // Bin lid
    drawRoundRect(
        color = Color(0xFF0369A1),
        topLeft = Offset(w * 0.18f, h * 0.34f),
        size = Size(w * 0.64f, h * 0.08f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    // Bin handle
    drawRoundRect(
        color = Color(0xFF075985),
        topLeft = Offset(w * 0.42f, h * 0.26f),
        size = Size(w * 0.16f, h * 0.09f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )

    // Candy wrapper falling into bin
    val candyCenter = Offset(w * 0.5f, h * (0.18f * pulse))
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xFFF43F5E), Color(0xFFE11D48))),
        radius = w * 0.10f,
        center = candyCenter
    )
    // Candy wrapper twists
    val leftWing = Path().apply {
        moveTo(candyCenter.x - w * 0.08f, candyCenter.y)
        lineTo(candyCenter.x - w * 0.18f, candyCenter.y - h * 0.06f)
        lineTo(candyCenter.x - w * 0.18f, candyCenter.y + h * 0.06f)
        close()
    }
    drawPath(leftWing, Color(0xFFFB7185))
    val rightWing = Path().apply {
        moveTo(candyCenter.x + w * 0.08f, candyCenter.y)
        lineTo(candyCenter.x + w * 0.18f, candyCenter.y - h * 0.06f)
        lineTo(candyCenter.x + w * 0.18f, candyCenter.y + h * 0.06f)
        close()
    }
    drawPath(rightWing, Color(0xFFFB7185))

    // Sparkle
    drawSparkle(w * 0.82f, h * 0.25f, w * 0.06f, Color(0xFFFBBF24))
}

private fun DrawScope.drawCupSink(w: Float, h: Float, pulse: Float) {
    // Sink basin
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))),
        topLeft = Offset(w * 0.15f, h * 0.55f),
        size = Size(w * 0.70f, h * 0.38f),
        cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
    )

    // Shiny Cup
    val cupPath = Path().apply {
        moveTo(w * 0.28f, h * 0.42f)
        lineTo(w * 0.50f, h * 0.42f)
        lineTo(w * 0.47f, h * 0.78f)
        lineTo(w * 0.31f, h * 0.78f)
        close()
    }
    drawPath(cupPath, Brush.horizontalGradient(listOf(Color(0xFFFB923C), Color(0xFFEA580C))))
    // Cup handle
    drawArc(
        color = Color(0xFFC2410C),
        startAngle = 270f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(w * 0.44f, h * 0.48f),
        size = Size(w * 0.14f, h * 0.22f),
        style = Stroke(width = w * 0.035f)
    )

    // Bottle beside it
    drawRoundRect(
        color = Color(0xFF38BDF8),
        topLeft = Offset(w * 0.56f, h * 0.36f),
        size = Size(w * 0.18f, h * 0.42f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawRoundRect(
        color = Color(0xFF0284C7),
        topLeft = Offset(w * 0.60f, h * 0.28f),
        size = Size(w * 0.10f, h * 0.09f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )

    // Water drops
    val dropCenter = Offset(w * 0.50f, h * 0.20f * pulse)
    drawCircle(Color(0xFF60A5FA), radius = w * 0.045f, center = dropCenter)
    drawSparkle(w * 0.22f, h * 0.30f, w * 0.05f, Color(0xFF38BDF8))
}

private fun DrawScope.drawTeddyOne(w: Float, h: Float, pulse: Float) {
    val scale = pulse
    val cx = w * 0.5f
    val cy = h * 0.58f

    // Ears
    drawCircle(Color(0xFFB45309), radius = w * 0.10f * scale, center = Offset(cx - w * 0.22f, cy - h * 0.28f))
    drawCircle(Color(0xFFFDE68A), radius = w * 0.05f * scale, center = Offset(cx - w * 0.22f, cy - h * 0.28f))
    drawCircle(Color(0xFFB45309), radius = w * 0.10f * scale, center = Offset(cx + w * 0.22f, cy - h * 0.28f))
    drawCircle(Color(0xFFFDE68A), radius = w * 0.05f * scale, center = Offset(cx + w * 0.22f, cy - h * 0.28f))

    // Body
    drawCircle(Color(0xFFD97706), radius = w * 0.26f, center = Offset(cx, cy + h * 0.12f))
    drawCircle(Color(0xFFFEF3C7), radius = w * 0.14f, center = Offset(cx, cy + h * 0.12f))

    // Paws (bottom)
    drawCircle(Color(0xFFB45309), radius = w * 0.09f, center = Offset(cx - w * 0.20f, cy + h * 0.28f))
    drawCircle(Color(0xFFB45309), radius = w * 0.09f, center = Offset(cx + w * 0.20f, cy + h * 0.28f))

    // Head
    drawCircle(Color(0xFFD97706), radius = w * 0.24f * scale, center = Offset(cx, cy - h * 0.12f))

    // Snout
    drawCircle(Color(0xFFFEF3C7), radius = w * 0.10f, center = Offset(cx, cy - h * 0.08f))
    // Nose
    drawCircle(Color(0xFF451A03), radius = w * 0.04f, center = Offset(cx, cy - h * 0.11f))
    // Eyes
    drawCircle(Color(0xFF1E293B), radius = w * 0.032f, center = Offset(cx - w * 0.08f, cy - h * 0.16f))
    drawCircle(Color(0xFF1E293B), radius = w * 0.032f, center = Offset(cx + w * 0.08f, cy - h * 0.16f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.012f, center = Offset(cx - w * 0.07f, cy - h * 0.17f))
    drawCircle(Color(0xFFFFFFFF), radius = w * 0.012f, center = Offset(cx + w * 0.09f, cy - h * 0.17f))

    // Heart on chest
    drawSparkle(cx, cy + h * 0.12f, w * 0.06f, Color(0xFFEF4444))
}

private fun DrawScope.drawBigTrash(w: Float, h: Float, pulse: Float) {
    // Large Trash Bag / Big debris
    drawOval(
        brush = Brush.radialGradient(listOf(Color(0xFF475569), Color(0xFF1E293B))),
        topLeft = Offset(w * 0.20f, h * 0.35f),
        size = Size(w * 0.60f, h * 0.55f)
    )
    // Tie on bag
    drawCircle(Color(0xFFEF4444), radius = w * 0.07f, center = Offset(w * 0.50f, h * 0.32f))

    // Broom beside it
    val broomStick = Path().apply {
        moveTo(w * 0.80f, h * 0.12f)
        lineTo(w * 0.68f, h * 0.70f)
    }
    drawPath(broomStick, Color(0xFFB45309), style = Stroke(width = w * 0.045f, cap = StrokeCap.Round))

    // Broom head
    drawRoundRect(
        color = Color(0xFFF59E0B),
        topLeft = Offset(w * 0.58f, h * 0.70f),
        size = Size(w * 0.22f, h * 0.18f),
        cornerRadius = CornerRadius(w * 0.03f, w * 0.03f)
    )

    drawSparkle(w * 0.22f, h * 0.22f, w * 0.07f, Color(0xFF34D399))
}

private fun DrawScope.drawBedTidy(w: Float, h: Float, pulse: Float) {
    // Bed frame
    drawRoundRect(
        color = Color(0xFF92400E),
        topLeft = Offset(w * 0.10f, h * 0.30f),
        size = Size(w * 0.80f, h * 0.58f),
        cornerRadius = CornerRadius(w * 0.05f, w * 0.05f)
    )
    // Mattress / Sheet
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF93C5FD), Color(0xFF3B82F6))),
        topLeft = Offset(w * 0.14f, h * 0.44f),
        size = Size(w * 0.72f, h * 0.40f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    // Folded Blanket top
    drawRoundRect(
        color = Color(0xFF60A5FA),
        topLeft = Offset(w * 0.14f, h * 0.50f),
        size = Size(w * 0.72f, h * 0.10f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )
    // Pillows
    drawRoundRect(
        color = Color(0xFFFFFFFF),
        topLeft = Offset(w * 0.18f, h * 0.34f),
        size = Size(w * 0.30f, h * 0.15f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawRoundRect(
        color = Color(0xFFFDE68A),
        topLeft = Offset(w * 0.52f, h * 0.34f),
        size = Size(w * 0.30f, h * 0.15f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    // Sparkle star above bed
    drawSparkle(w * 0.50f, h * 0.18f * pulse, w * 0.07f, Color(0xFFFBBF24))
}

private fun DrawScope.drawSweepRoom(w: Float, h: Float, pulse: Float) {
    // Broom Handle
    val broomStick = Path().apply {
        moveTo(w * 0.30f, h * 0.15f)
        lineTo(w * 0.65f, h * 0.65f)
    }
    drawPath(broomStick, Color(0xFF854D0E), style = Stroke(width = w * 0.05f, cap = StrokeCap.Round))

    // Broom Bristles
    val bristles = Path().apply {
        moveTo(w * 0.58f, h * 0.60f)
        lineTo(w * 0.85f, h * 0.72f)
        lineTo(w * 0.72f, h * 0.90f)
        lineTo(w * 0.48f, h * 0.75f)
        close()
    }
    drawPath(bristles, Brush.linearGradient(listOf(Color(0xFFFBBF24), Color(0xFFD97706))))

    // Swept Clean Stars
    drawSparkle(w * 0.28f, h * 0.68f, w * 0.07f, Color(0xFF38BDF8))
    drawSparkle(w * 0.40f, h * 0.84f, w * 0.05f, Color(0xFFF472B6))
    drawSparkle(w * 0.18f, h * 0.45f, w * 0.06f, Color(0xFF34D399))
}

private fun DrawScope.drawSofaPillows(w: Float, h: Float, pulse: Float) {
    // Sofa Backrest
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFFA855F7), Color(0xFF7E22CE))),
        topLeft = Offset(w * 0.12f, h * 0.28f),
        size = Size(w * 0.76f, h * 0.35f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
    )
    // Sofa Seat Base
    drawRoundRect(
        color = Color(0xFF6B21A8),
        topLeft = Offset(w * 0.08f, h * 0.55f),
        size = Size(w * 0.84f, h * 0.25f),
        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
    )
    // Sofa Arms
    drawRoundRect(
        color = Color(0xFF581C87),
        topLeft = Offset(w * 0.06f, h * 0.45f),
        size = Size(w * 0.14f, h * 0.35f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawRoundRect(
        color = Color(0xFF581C87),
        topLeft = Offset(w * 0.80f, h * 0.45f),
        size = Size(w * 0.14f, h * 0.35f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )

    // Neat Fluffy Pillows
    drawRoundRect(
        color = Color(0xFFF472B6),
        topLeft = Offset(w * 0.22f, h * 0.44f),
        size = Size(w * 0.24f, h * 0.22f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawRoundRect(
        color = Color(0xFFFBBF24),
        topLeft = Offset(w * 0.54f, h * 0.44f),
        size = Size(w * 0.24f, h * 0.22f),
        cornerRadius = CornerRadius(w * 0.04f, w * 0.04f)
    )
    drawSparkle(w * 0.50f, h * 0.20f * pulse, w * 0.06f, Color(0xFFFACC15))
}

private fun DrawScope.drawTeddyGroup(w: Float, h: Float, pulse: Float) {
    // Bear left (Blue)
    drawCircle(Color(0xFF3B82F6), radius = w * 0.16f, center = Offset(w * 0.26f, h * 0.55f))
    drawCircle(Color(0xFF93C5FD), radius = w * 0.07f, center = Offset(w * 0.26f, h * 0.55f))
    drawCircle(Color(0xFF1E3A8A), radius = w * 0.06f, center = Offset(w * 0.16f, h * 0.38f))
    drawCircle(Color(0xFF1E3A8A), radius = w * 0.06f, center = Offset(w * 0.34f, h * 0.38f))

    // Bear right (Pink Bunny)
    drawCircle(Color(0xFFEC4899), radius = w * 0.16f, center = Offset(w * 0.74f, h * 0.55f))
    drawCircle(Color(0xFFFBCFE8), radius = w * 0.07f, center = Offset(w * 0.74f, h * 0.55f))
    // Bunny long ears
    drawOval(Color(0xFFDB2777), topLeft = Offset(w * 0.64f, h * 0.24f), size = Size(w * 0.07f, h * 0.20f))
    drawOval(Color(0xFFDB2777), topLeft = Offset(w * 0.77f, h * 0.24f), size = Size(w * 0.07f, h * 0.20f))

    // Main Bear Center (Golden brown)
    drawCircle(Color(0xFFD97706), radius = w * 0.22f * pulse, center = Offset(w * 0.50f, h * 0.50f))
    drawCircle(Color(0xFFFEF3C7), radius = w * 0.10f, center = Offset(w * 0.50f, h * 0.52f))
    drawCircle(Color(0xFF451A03), radius = w * 0.035f, center = Offset(w * 0.50f, h * 0.48f))
    // Center bear ears
    drawCircle(Color(0xFFB45309), radius = w * 0.08f, center = Offset(w * 0.35f, h * 0.32f))
    drawCircle(Color(0xFFB45309), radius = w * 0.08f, center = Offset(w * 0.65f, h * 0.32f))

    // Sparkles
    drawSparkle(w * 0.50f, h * 0.15f, w * 0.07f, Color(0xFFFBBF24))
    drawSparkle(w * 0.15f, h * 0.25f, w * 0.05f, Color(0xFF34D399))
}

private fun DrawScope.drawToysBox(w: Float, h: Float, pulse: Float) {
    // Open Toy Box
    val boxBrush = Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF047857)))
    val boxPath = Path().apply {
        moveTo(w * 0.12f, h * 0.48f)
        lineTo(w * 0.88f, h * 0.48f)
        lineTo(w * 0.80f, h * 0.88f)
        lineTo(w * 0.20f, h * 0.88f)
        close()
    }
    drawPath(boxPath, boxBrush)

    // Box rim
    drawRoundRect(
        color = Color(0xFF065F46),
        topLeft = Offset(w * 0.10f, h * 0.44f),
        size = Size(w * 0.80f, h * 0.09f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )

    // Toys popping out: Ball, Blocks, Teddy
    // Red Ball
    drawCircle(Color(0xFFEF4444), radius = w * 0.12f, center = Offset(w * 0.30f, h * 0.36f))
    drawArc(
        color = Color(0xFFFDE047),
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = false,
        topLeft = Offset(w * 0.22f, h * 0.28f),
        size = Size(w * 0.16f, h * 0.16f),
        style = Stroke(width = w * 0.03f)
    )

    // Building Blocks
    drawRoundRect(
        color = Color(0xFF3B82F6),
        topLeft = Offset(w * 0.48f, h * 0.25f),
        size = Size(w * 0.18f, h * 0.18f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )
    drawRoundRect(
        color = Color(0xFFF59E0B),
        topLeft = Offset(w * 0.65f, h * 0.32f),
        size = Size(w * 0.16f, h * 0.16f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )

    drawSparkle(w * 0.50f, h * 0.12f * pulse, w * 0.07f, Color(0xFFFBBF24))
}

private fun DrawScope.drawSweepLiving(w: Float, h: Float, pulse: Float) {
    // Floor tiles perspective
    val floorBrush = Brush.verticalGradient(listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A)))
    val floorPath = Path().apply {
        moveTo(w * 0.10f, h * 0.50f)
        lineTo(w * 0.90f, h * 0.50f)
        lineTo(w * 0.95f, h * 0.90f)
        lineTo(w * 0.05f, h * 0.90f)
        close()
    }
    drawPath(floorPath, floorBrush)

    // Clean light rays
    drawSparkle(w * 0.30f, h * 0.65f, w * 0.09f, Color(0xFF38BDF8))
    drawSparkle(w * 0.70f, h * 0.68f, w * 0.08f, Color(0xFFF59E0B))
    drawSparkle(w * 0.50f, h * 0.78f, w * 0.10f, Color(0xFF10B981))

    // Cheerful broom
    val broomStick = Path().apply {
        moveTo(w * 0.50f, h * 0.15f)
        lineTo(w * 0.35f, h * 0.58f)
    }
    drawPath(broomStick, Color(0xFFB45309), style = Stroke(width = w * 0.045f, cap = StrokeCap.Round))
    drawRoundRect(
        color = Color(0xFFEA580C),
        topLeft = Offset(w * 0.25f, h * 0.58f),
        size = Size(w * 0.22f, h * 0.14f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )

    drawSparkle(w * 0.75f, h * 0.25f * pulse, w * 0.08f, Color(0xFFEC4899))
}

private fun DrawScope.drawLivingRoomClean(w: Float, h: Float, pulse: Float) {
    // Living Room scene with shining crown/star
    // Lamp
    drawRoundRect(
        color = Color(0xFFCBD5E1),
        topLeft = Offset(w * 0.15f, h * 0.30f),
        size = Size(w * 0.05f, h * 0.50f),
        cornerRadius = CornerRadius(w * 0.01f, w * 0.01f)
    )
    val lampShade = Path().apply {
        moveTo(w * 0.10f, h * 0.32f)
        lineTo(w * 0.25f, h * 0.32f)
        lineTo(w * 0.21f, h * 0.18f)
        lineTo(w * 0.14f, h * 0.18f)
        close()
    }
    drawPath(lampShade, Color(0xFFF59E0B))

    // Clean Cozy Sofa
    drawRoundRect(
        brush = Brush.verticalGradient(listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))),
        topLeft = Offset(w * 0.30f, h * 0.45f),
        size = Size(w * 0.60f, h * 0.38f),
        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
    )

    // Giant Sparkling Clean Diamond / Star in Center
    drawSparkle(w * 0.55f, h * 0.25f * pulse, w * 0.12f, Color(0xFFFBBF24))
    drawSparkle(w * 0.85f, h * 0.35f, w * 0.07f, Color(0xFF34D399))
    drawSparkle(w * 0.35f, h * 0.35f, w * 0.06f, Color(0xFFEC4899))
}

private fun DrawScope.drawPlantWater(w: Float, h: Float, pulse: Float) {
    // Flower Pot
    val potPath = Path().apply {
        moveTo(w * 0.32f, h * 0.55f)
        lineTo(w * 0.68f, h * 0.55f)
        lineTo(w * 0.60f, h * 0.88f)
        lineTo(w * 0.40f, h * 0.88f)
        close()
    }
    drawPath(potPath, Color(0xFFEA580C))

    // Flower Stem & Leaves
    drawRoundRect(
        color = Color(0xFF16A34A),
        topLeft = Offset(w * 0.47f, h * 0.32f),
        size = Size(w * 0.06f, h * 0.25f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )
    drawOval(Color(0xFF22C55E), topLeft = Offset(w * 0.32f, h * 0.42f), size = Size(w * 0.16f, h * 0.08f))

    // Happy Flower Petals
    drawCircle(Color(0xFFEC4899), radius = w * 0.08f, center = Offset(w * 0.50f, h * 0.22f))
    drawCircle(Color(0xFFFDE047), radius = w * 0.045f, center = Offset(w * 0.50f, h * 0.22f))

    drawSparkle(w * 0.75f, h * 0.25f * pulse, w * 0.06f, Color(0xFF38BDF8))
}

private fun DrawScope.drawShoesRack(w: Float, h: Float, pulse: Float) {
    // Shoe Rack Shelf
    drawRoundRect(
        color = Color(0xFF78350F),
        topLeft = Offset(w * 0.10f, h * 0.65f),
        size = Size(w * 0.80f, h * 0.10f),
        cornerRadius = CornerRadius(w * 0.02f, w * 0.02f)
    )
    // Left shoe (Red Sneaker)
    drawRoundRect(
        color = Color(0xFFEF4444),
        topLeft = Offset(w * 0.18f, h * 0.45f),
        size = Size(w * 0.28f, h * 0.20f),
        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
    )
    // Right shoe (Blue Sneaker)
    drawRoundRect(
        color = Color(0xFF3B82F6),
        topLeft = Offset(w * 0.54f, h * 0.45f),
        size = Size(w * 0.28f, h * 0.20f),
        cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
    )
    drawSparkle(w * 0.50f, h * 0.25f * pulse, w * 0.07f, Color(0xFFFBBF24))
}

private fun DrawScope.drawCustomChore(w: Float, h: Float, pulse: Float) {
    // Magic Star Wand
    val wandPath = Path().apply {
        moveTo(w * 0.25f, h * 0.80f)
        lineTo(w * 0.65f, h * 0.35f)
    }
    drawPath(wandPath, Color(0xFF8B5CF6), style = Stroke(width = w * 0.06f, cap = StrokeCap.Round))
    drawSparkle(w * 0.70f, h * 0.30f * pulse, w * 0.16f, Color(0xFFFBBF24))
    drawSparkle(w * 0.35f, h * 0.35f, w * 0.07f, Color(0xFFEC4899))
    drawSparkle(w * 0.75f, h * 0.65f, w * 0.06f, Color(0xFF38BDF8))
}

fun DrawScope.drawSparkle(cx: Float, cy: Float, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(cx, cy - radius)
        quadraticTo(cx, cy, cx + radius, cy)
        quadraticTo(cx, cy, cx, cy + radius)
        quadraticTo(cx, cy, cx - radius, cy)
        quadraticTo(cx, cy, cx, cy - radius)
        close()
    }
    drawPath(path, color, style = Fill)
}
