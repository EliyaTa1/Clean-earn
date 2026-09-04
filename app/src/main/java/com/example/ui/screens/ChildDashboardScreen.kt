package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreCategory
import com.example.data.model.ChoreEntity
import com.example.ui.components.ChoreIllustration
import com.example.ui.components.ChildAvatarView
import com.example.ui.components.HoldToLaunchButton
import com.example.ui.components.ParticleOverlay
import com.example.ui.components.VisualBankMeter

@Composable
fun ChildDashboardScreen(
    child: ChildEntity,
    chores: List<ChoreEntity>,
    onChoreTapped: (ChoreEntity) -> Unit,
    onStartScreenTime: () -> Unit,
    onHoldTick: (step: Int) -> Unit,
    onBackToChildSelect: () -> Unit,
    particleTrigger: Long,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1E1B4B), // Midnight Blue
                        Color(0xFF0F172A)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header: Child Info & Back Switch Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Child Avatar & Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChildAvatarView(
                        avatarKey = child.avatarKey,
                        modifier = Modifier.size(54.dp),
                        backgroundColor = Color(child.colorHex).copy(alpha = 0.2f),
                        borderColor = Color(child.colorHex),
                        borderWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = child.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "בוא נסדר ונרוויח!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF93C5FD)
                        )
                    }
                }

                // Switch Child / Back Button
                Surface(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable(onClick = onBackToChildSelect)
                        .testTag("switch_child_button"),
                    color = Color(0xFF334155),
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier.size(46.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Switch Profile",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Visual Bank Meter (The filling container: liquid wave, coins, minutes)
            VisualBankMeter(
                currentBalanceMinutes = child.currentBalanceMinutes,
                todayEarnedMinutes = child.todayEarnedMinutes,
                dailyCapMinutes = child.dailyCapMinutes,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Visual Chores Grid (zero text needed, big clear illustrations)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(chores, key = { it.id }) { chore ->
                    VisualChoreCard(
                        chore = chore,
                        onTap = { onChoreTapped(chore) }
                    )
                }
            }

            // Giant Prominent Green "Start Screen Time" Button (2-sec hold to prevent misclicks)
            HoldToLaunchButton(
                balanceMinutes = child.currentBalanceMinutes,
                onLaunch = onStartScreenTime,
                onHoldTick = onHoldTick,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        // Particle Burst Overlay on top
        ParticleOverlay(trigger = particleTrigger)
    }
}

@Composable
fun VisualChoreCard(
    chore: ChoreEntity,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }
    var isCelebrating by remember { mutableStateOf(false) }

    // Subtle scale-up spring animation for the whole card
    val cardScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.93f
            isCelebrating -> 1.06f
            else -> 1.0f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "card_scale"
    )

    // Dedicated scale-up pop and playful rotation for the chore icon
    val iconScale by animateFloatAsState(
        targetValue = if (isCelebrating) 1.22f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "icon_scale"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isCelebrating) -6f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "icon_rotation"
    )

    val overlayAlpha by animateFloatAsState(
        targetValue = if (isCelebrating) 1f else 0f,
        animationSpec = tween(150),
        label = "overlay_alpha"
    )

    val overlayScale by animateFloatAsState(
        targetValue = if (isCelebrating) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "overlay_scale"
    )

    val defaultBadgeColor = when (chore.category) {
        ChoreCategory.QUICK -> Color(0xFF0284C7) // Sky Blue
        ChoreCategory.SMALL -> Color(0xFF059669) // Emerald Green
        ChoreCategory.BIG -> Color(0xFF7C3AED)   // Violet
    }

    val cardBg = when (chore.category) {
        ChoreCategory.QUICK -> Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
        ChoreCategory.SMALL -> Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))
        ChoreCategory.BIG -> Brush.verticalGradient(listOf(Color(0xFF2E1065), Color(0xFF1E1B4B)))
    }

    val glowBorderBrush = Brush.linearGradient(
        listOf(
            Color(0xFF34D399), // Emerald
            Color(0xFFFBBF24), // Gold
            Color(0xFF38BDF8), // Cyan
            Color(0xFF34D399)
        )
    )

    Card(
        modifier = modifier
            .scale(cardScale)
            .clip(RoundedCornerShape(22.dp))
            .then(
                if (isCelebrating) {
                    Modifier.border(2.5.dp, glowBorderBrush, RoundedCornerShape(22.dp))
                } else {
                    Modifier
                }
            )
            .clickable {
                onTap() // Immediate 0ms execution
                coroutineScope.launch {
                    isPressed = true
                    delay(70)
                    isPressed = false
                    isCelebrating = true
                    delay(900)
                    isCelebrating = false
                }
            }
            .shadow(if (isCelebrating) 12.dp else 6.dp, RoundedCornerShape(22.dp))
            .testTag("chore_card_${chore.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardBg)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Top Reward / Status Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCelebrating) Color(0xFF059669) else defaultBadgeColor,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isCelebrating) "+${chore.minutesReward} נוספו!" else "+${chore.minutesReward}",
                            fontSize = if (isCelebrating) 12.sp else 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = if (isCelebrating) Icons.Default.Check else Icons.Default.Star,
                            contentDescription = if (isCelebrating) "Done" else "Star",
                            tint = if (isCelebrating) Color.White else Color(0xFFFDE047),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Big Visual Vector Illustration with scale-up pop & playful tilt
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .scale(iconScale)
                        .rotate(iconRotation)
                ) {
                    ChoreIllustration(
                        icon = chore.icon,
                        modifier = Modifier
                            .size(76.dp)
                            .padding(vertical = 4.dp)
                    )
                }

                // Bottom title (clean, helpful for young children and parents)
                Text(
                    text = chore.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCelebrating) Color(0xFF6EE7B7) else Color(0xFFE2E8F0),
                    maxLines = 1
                )
            }

            // Celebratory Success State Overlay Badge
            if (overlayAlpha > 0.01f) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xF2064E3B),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = overlayAlpha
                            scaleX = overlayScale
                            scaleY = overlayScale
                        }
                        .border(1.5.dp, Color(0xFF34D399), RoundedCornerShape(16.dp))
                        .padding(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "סיימתי",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "סיימתי!",
                            color = Color(0xFFF0FDF4),
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
