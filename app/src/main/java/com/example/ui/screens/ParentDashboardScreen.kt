package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreCategory
import com.example.data.model.ChoreEntity
import com.example.data.model.ChoreHistoryEntity
import com.example.data.model.ChoreIcon
import com.example.data.model.ParentSettingsEntity
import com.example.ui.components.ChoreIllustration
import com.example.ui.components.ChildAvatarView
import com.example.ui.components.HebrewKeyboard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    children: List<ChildEntity>,
    chores: List<ChoreEntity>,
    settings: ParentSettingsEntity,
    history: List<ChoreHistoryEntity>,
    onSaveChild: (ChildEntity) -> Unit,
    onDeleteChild: (ChildEntity) -> Unit,
    onResetChildDay: (Long) -> Unit,
    onSaveChore: (ChoreEntity) -> Unit,
    onDeleteChore: (ChoreEntity) -> Unit,
    onSaveSettings: (ParentSettingsEntity) -> Unit,
    onResetAllToday: () -> Unit,
    onExitParentMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddChildDialog by remember { mutableStateOf(false) }
    var editingChild by remember { mutableStateOf<ChildEntity?>(null) }
    var showAddChoreDialog by remember { mutableStateOf(false) }
    var editingChore by remember { mutableStateOf<ChoreEntity?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onExitParentMode,
                        modifier = Modifier.testTag("exit_parent_dashboard")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "בקרת הורים",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "ניהול ילדים, משימות והגדרות",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // Tab Bar
            val tabs = listOf(
                "ילדים" to Icons.Default.People,
                "משימות" to Icons.Default.CleaningServices,
                "הגדרות" to Icons.Default.Settings,
                "יומן" to Icons.Default.History
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF1E293B),
                contentColor = Color(0xFF38BDF8),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF38BDF8)
                    )
                }
            ) {
                tabs.forEachIndexed { index, (title, icon) ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        },
                        selectedContentColor = Color(0xFF38BDF8),
                        unselectedContentColor = Color(0xFF94A3B8)
                    )
                }
            }

            // Tab Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> ChildrenManagementTab(
                        children = children,
                        onAddChild = {
                            editingChild = null
                            showAddChildDialog = true
                        },
                        onEditChild = { child ->
                            editingChild = child
                            showAddChildDialog = true
                        },
                        onDeleteChild = onDeleteChild,
                        onResetChildDay = onResetChildDay
                    )
                    1 -> ChoresManagementTab(
                        chores = chores,
                        onAddChore = {
                            editingChore = null
                            showAddChoreDialog = true
                        },
                        onEditChore = { chore ->
                            editingChore = chore
                            showAddChoreDialog = true
                        },
                        onToggleChore = { chore ->
                            onSaveChore(chore.copy(isEnabled = !chore.isEnabled))
                        },
                        onDeleteChore = onDeleteChore
                    )
                    2 -> SettingsTab(
                        settings = settings,
                        onSaveSettings = onSaveSettings,
                        onResetAllToday = onResetAllToday
                    )
                    3 -> HistoryTab(history = history)
                }
            }
        }

        // Child Add/Edit Dialog
        if (showAddChildDialog) {
            ChildEditDialog(
                initialChild = editingChild,
                defaultDailyCap = settings.defaultDailyCapMinutes,
                onDismiss = { showAddChildDialog = false },
                onSave = { savedChild ->
                    onSaveChild(savedChild)
                    showAddChildDialog = false
                }
            )
        }

        // Chore Add/Edit Dialog
        if (showAddChoreDialog) {
            ChoreEditDialog(
                initialChore = editingChore,
                onDismiss = { showAddChoreDialog = false },
                onSave = { savedChore ->
                    onSaveChore(savedChore)
                    showAddChoreDialog = false
                }
            )
        }
    }
}

// -------------------------------------------------------------
// TAB 1: Children Management
// -------------------------------------------------------------
@Composable
fun ChildrenManagementTab(
    children: List<ChildEntity>,
    onAddChild: () -> Unit,
    onEditChild: (ChildEntity) -> Unit,
    onDeleteChild: (ChildEntity) -> Unit,
    onResetChildDay: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onAddChild,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("add_child_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text("הוספת ילד/ה חדש/ה", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (children.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("עדיין לא נוספו ילדים. לחצו על הכפתור למעלה להוספה.", color = Color(0xFF94A3B8))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(children, key = { it.id }) { child ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ChildAvatarView(
                                    avatarKey = child.avatarKey,
                                    modifier = Modifier.size(54.dp),
                                    backgroundColor = Color(child.colorHex).copy(alpha = 0.2f),
                                    borderColor = Color(child.colorHex)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = child.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "יתרה: ${child.currentBalanceMinutes} דק' | תקרה יומית: ${child.dailyCapMinutes} דק'",
                                        fontSize = 13.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                            }

                            Row {
                                IconButton(
                                    onClick = { onResetChildDay(child.id) },
                                    modifier = Modifier.testTag("reset_child_${child.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestartAlt,
                                        contentDescription = "Reset balance",
                                        tint = Color(0xFFF59E0B)
                                    )
                                }
                                IconButton(
                                    onClick = { onEditChild(child) },
                                    modifier = Modifier.testTag("edit_child_${child.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = Color(0xFF38BDF8)
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteChild(child) },
                                    modifier = Modifier.testTag("delete_child_${child.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: Chores Management
// -------------------------------------------------------------
@Composable
fun ChoresManagementTab(
    chores: List<ChoreEntity>,
    onAddChore: () -> Unit,
    onEditChore: (ChoreEntity) -> Unit,
    onToggleChore: (ChoreEntity) -> Unit,
    onDeleteChore: (ChoreEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onAddChore,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("add_chore_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text("הוספת משימה חדשה", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(chores, key = { it.id }) { chore ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (chore.isEnabled) Color(0xFF1E293B) else Color(0xFF0F172A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ChoreIllustration(
                                icon = chore.icon,
                                modifier = Modifier.size(46.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = chore.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (chore.isEnabled) Color.White else Color(0xFF64748B)
                                )
                                Text(
                                    text = "${chore.minutesReward} דקות | ${
                                        when (chore.category) {
                                            ChoreCategory.QUICK -> "שוטפת (2-3 דק')"
                                            ChoreCategory.SMALL -> "קטנה (5 דק')"
                                            ChoreCategory.BIG -> "גדולה (10 דק')"
                                        }
                                    }",
                                    fontSize = 12.sp,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = chore.isEnabled,
                                onCheckedChange = { onToggleChore(chore) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFF10B981)
                                )
                            )
                            IconButton(onClick = { onEditChore(chore) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(0xFF38BDF8)
                                )
                            }
                            IconButton(onClick = { onDeleteChore(chore) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: Settings & Daily Limits
// -------------------------------------------------------------
@Composable
fun SettingsTab(
    settings: ParentSettingsEntity,
    onSaveSettings: (ParentSettingsEntity) -> Unit,
    onResetAllToday: () -> Unit
) {
    var pinCode by remember(settings) { mutableStateOf(settings.pinCode) }
    var dailyCap by remember(settings) { mutableIntStateOf(settings.defaultDailyCapMinutes) }
    var voiceEnabled by remember(settings) { mutableStateOf(settings.voiceSpeechEnabled) }
    var soundsEnabled by remember(settings) { mutableStateOf(settings.soundEffectsEnabled) }
    var countdownVisual by remember(settings) { mutableStateOf(settings.countdownVisual) }
    var showSavedMessage by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Daily Limit Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "מגבלת זמן יומית מקסימלית: $dailyCap דקות",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "תקרה מקסימלית לצבירת זמן מסך ביום לילד",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Slider(
                        value = dailyCap.toFloat(),
                        onValueChange = { dailyCap = it.toInt() },
                        valueRange = 15f..120f,
                        steps = 6,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF38BDF8),
                            activeTrackColor = Color(0xFF0284C7)
                        )
                    )
                }
            }
        }

        item {
            // Visual Countdown Theme
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "גרף מתרוקן בזמן מסך",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            "ICE_CREAM" to "גלידה נאכלת 🍦",
                            "BATTERY" to "סוללה יורדת 🔋",
                            "STAR_JAR" to "מיכל כוכבים ✨"
                        ).forEach { (key, label) ->
                            FilterChip(
                                selected = countdownVisual == key,
                                onClick = { countdownVisual = key },
                                label = { Text(label, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4F46E5),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        item {
            // Speech & Sound toggles
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("חיווי קולי בעברית לילד", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("הקראת עידוד בעת סיום משימה", color = Color(0xFF94A3B8), fontSize = 12.sp)
                        }
                        Switch(
                            checked = voiceEnabled,
                            onCheckedChange = { voiceEnabled = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("אפקטים קוליים ומנגינות", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("צלילי מטבעות, תרועות וסיום", color = Color(0xFF94A3B8), fontSize = 12.sp)
                        }
                        Switch(
                            checked = soundsEnabled,
                            onCheckedChange = { soundsEnabled = it }
                        )
                    }
                }
            }
        }

        item {
            // PIN code
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("קוד PIN של ההורים (4 ספרות)", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { if (it.length <= 4) pinCode = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )
                }
            }
        }

        item {
            // Save Settings Button
            Button(
                onClick = {
                    onSaveSettings(
                        settings.copy(
                            pinCode = pinCode.ifBlank { "1234" },
                            defaultDailyCapMinutes = dailyCap,
                            voiceSpeechEnabled = voiceEnabled,
                            soundEffectsEnabled = soundsEnabled,
                            countdownVisual = countdownVisual
                        )
                    )
                    showSavedMessage = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_settings_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                Spacer(modifier = Modifier.width(8.dp))
                Text("שמור הגדרות", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (showSavedMessage) {
                Text(
                    text = "ההגדרות נשמרו בהצלחה! ✓",
                    color = Color(0xFF34D399),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }

        item {
            // Reset Today Button
            Button(
                onClick = onResetAllToday,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F1D1D))
            ) {
                Icon(imageVector = Icons.Default.RestartAlt, contentDescription = "Reset")
                Spacer(modifier = Modifier.width(8.dp))
                Text("איפוס כל נתוני היום לכל הילדים", color = Color(0xFFFECACA), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// -------------------------------------------------------------
// TAB 4: History
// -------------------------------------------------------------
@Composable
fun HistoryTab(history: List<ChoreHistoryEntity>) {
    if (history.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("אין היסטוריית פעילות עדיין", color = Color(0xFF94A3B8))
        }
    } else {
        val sdf = SimpleDateFormat("HH:mm - dd/MM", Locale.getDefault())
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(history, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${item.childName} • ${item.choreTitle}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = sdf.format(Date(item.timestamp)),
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF065F46)
                        ) {
                            Text(
                                text = "+${item.minutesAdded} דק'",
                                color = Color(0xFF6EE7B7),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Dialogs: Child Edit & Chore Edit
// -------------------------------------------------------------
@Composable
fun ChildEditDialog(
    initialChild: ChildEntity?,
    defaultDailyCap: Int,
    onDismiss: () -> Unit,
    onSave: (ChildEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialChild?.name ?: "") }
    var avatarKey by remember { mutableStateOf(initialChild?.avatarKey ?: "teddy") }
    var dailyCap by remember { mutableIntStateOf(initialChild?.dailyCapMinutes ?: defaultDailyCap) }
    var balanceMinutes by remember { mutableIntStateOf(initialChild?.currentBalanceMinutes ?: 0) }
    var showHebrewKeyboard by remember { mutableStateOf(false) }

    val nameSuggestions = listOf("איתי", "נועה", "יונתן", "מאיה", "ליאן", "אריאל", "רועי", "תמר", "דניאל", "עומר", "שירה", "גיא")
    val avatars = listOf("teddy", "bunny", "lion", "cat", "puppy", "rocket", "dino", "star")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialChild == null) "הוספת ילד/ה" else "עריכת ${initialChild.name}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        containerColor = Color(0xFF1E1B4B),
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("שם הילד/ה") },
                        trailingIcon = {
                            IconButton(onClick = { showHebrewKeyboard = !showHebrewKeyboard }) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "מקלדת עברית",
                                    tint = if (showHebrewKeyboard) Color(0xFF38BDF8) else Color(0xFF94A3B8)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )
                }

                item {
                    // Hebrew Virtual Keyboard helper
                    if (showHebrewKeyboard) {
                        HebrewKeyboard(
                            onCharTyped = { char -> name += char },
                            onBackspace = { if (name.isNotEmpty()) name = name.dropLast(1) },
                            onSpace = { name += " " },
                            onClear = { name = "" },
                            onQuickSuggestion = { selected -> name = selected },
                            suggestions = nameSuggestions
                        )
                    } else {
                        // Quick name selection chips if keyboard is closed
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("שמות נפוצים:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                            TextButton(
                                onClick = { showHebrewKeyboard = true },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("פתח מקלדת עברית", fontSize = 12.sp, color = Color(0xFF38BDF8))
                            }
                        }
                    }
                }

                item {
                    Text("בחירת דמות אישית:", color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        avatars.take(4).forEach { key ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable { avatarKey = key }
                                    .background(if (avatarKey == key) Color(0xFF6366F1) else Color.Transparent)
                                    .padding(3.dp)
                            ) {
                                ChildAvatarView(avatarKey = key, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        avatars.drop(4).forEach { key ->
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .clickable { avatarKey = key }
                                    .background(if (avatarKey == key) Color(0xFF6366F1) else Color.Transparent)
                                    .padding(3.dp)
                            ) {
                                ChildAvatarView(avatarKey = key, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }

                item {
                    Text("תקרה יומית: $dailyCap דקות", color = Color.White, fontSize = 13.sp)
                    Slider(
                        value = dailyCap.toFloat(),
                        onValueChange = { dailyCap = it.toInt() },
                        valueRange = 15f..120f,
                        steps = 6
                    )
                }

                item {
                    Text("יתרת דקות נוכחית: $balanceMinutes דקות", color = Color.White, fontSize = 13.sp)
                    Slider(
                        value = balanceMinutes.toFloat(),
                        onValueChange = { balanceMinutes = it.toInt() },
                        valueRange = 0f..90f,
                        steps = 8
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val child = initialChild?.copy(
                            name = name.trim(),
                            avatarKey = avatarKey,
                            dailyCapMinutes = dailyCap,
                            currentBalanceMinutes = balanceMinutes
                        ) ?: ChildEntity(
                            name = name.trim(),
                            avatarKey = avatarKey,
                            dailyCapMinutes = dailyCap,
                            currentBalanceMinutes = balanceMinutes
                        )
                        onSave(child)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Text("שמור", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ביטול", color = Color(0xFF94A3B8))
            }
        }
    )
}

@Composable
fun ChoreEditDialog(
    initialChore: ChoreEntity?,
    onDismiss: () -> Unit,
    onSave: (ChoreEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialChore?.title ?: "") }
    var voiceHebrew by remember { mutableStateOf(initialChore?.voiceHebrew ?: "") }
    var minutesReward by remember { mutableIntStateOf(initialChore?.minutesReward ?: 5) }
    var category by remember { mutableStateOf(initialChore?.category ?: ChoreCategory.SMALL) }
    var icon by remember { mutableStateOf(initialChore?.icon ?: ChoreIcon.CUSTOM) }
    var focusedField by remember { mutableStateOf("title") } // "title" or "voiceHebrew"
    var showHebrewKeyboard by remember { mutableStateOf(false) }

    val choreSuggestions = listOf(
        "זריקת עטיפה לפח",
        "כוס לכיור",
        "הנחת בובה במיטה",
        "שקית זבל גדולה",
        "סידור מיטה",
        "טיטוא החדר",
        "סידור כריות בסלון",
        "איסוף צעצועים",
        "סידור יסודי בסלון",
        "שטיפת ידיים",
        "צחצוח שיניים"
    )

    val icons = listOf(
        ChoreIcon.TRASH_WRAPPER,
        ChoreIcon.CUP_SINK,
        ChoreIcon.TEDDY_ONE,
        ChoreIcon.BIG_TRASH,
        ChoreIcon.BED_TIDY,
        ChoreIcon.SWEEP_ROOM,
        ChoreIcon.SOFA_PILLOWS,
        ChoreIcon.TEDDY_GROUP,
        ChoreIcon.TOYS_BOX,
        ChoreIcon.SWEEP_LIVING,
        ChoreIcon.LIVING_ROOM_CLEAN,
        ChoreIcon.CUSTOM
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialChore == null) "הוספת משימה" else "עריכת משימה",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        containerColor = Color(0xFF1E1B4B),
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("שם המשימה") },
                        trailingIcon = {
                            IconButton(onClick = {
                                focusedField = "title"
                                showHebrewKeyboard = !showHebrewKeyboard
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "מקלדת עברית",
                                    tint = if (showHebrewKeyboard && focusedField == "title") Color(0xFF38BDF8) else Color(0xFF94A3B8)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = voiceHebrew,
                        onValueChange = { voiceHebrew = it },
                        label = { Text("משפט עידוד קולי (TTS)") },
                        trailingIcon = {
                            IconButton(onClick = {
                                focusedField = "voiceHebrew"
                                showHebrewKeyboard = !showHebrewKeyboard
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = "מקלדת עברית",
                                    tint = if (showHebrewKeyboard && focusedField == "voiceHebrew") Color(0xFF38BDF8) else Color(0xFF94A3B8)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF475569)
                        )
                    )
                }

                item {
                    if (showHebrewKeyboard) {
                        HebrewKeyboard(
                            onCharTyped = { char ->
                                if (focusedField == "title") {
                                    title += char
                                } else {
                                    voiceHebrew += char
                                }
                            },
                            onBackspace = {
                                if (focusedField == "title" && title.isNotEmpty()) {
                                    title = title.dropLast(1)
                                } else if (focusedField == "voiceHebrew" && voiceHebrew.isNotEmpty()) {
                                    voiceHebrew = voiceHebrew.dropLast(1)
                                }
                            },
                            onSpace = {
                                if (focusedField == "title") {
                                    title += " "
                                } else {
                                    voiceHebrew += " "
                                }
                            },
                            onClear = {
                                if (focusedField == "title") {
                                    title = ""
                                } else {
                                    voiceHebrew = ""
                                }
                            },
                            onQuickSuggestion = { selected ->
                                if (focusedField == "title") {
                                    title = selected
                                    if (voiceHebrew.isBlank()) {
                                        voiceHebrew = "כל הכבוד! סיימת $selected!"
                                    }
                                } else {
                                    voiceHebrew = selected
                                }
                            },
                            suggestions = choreSuggestions
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { showHebrewKeyboard = true },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Keyboard,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF38BDF8)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("הצג מקלדת עברית", fontSize = 12.sp, color = Color(0xFF38BDF8))
                            }
                        }
                    }
                }

                item {
                    Text("ניקוד: $minutesReward דקות", color = Color.White, fontSize = 14.sp)
                    Slider(
                        value = minutesReward.toFloat(),
                        onValueChange = {
                            minutesReward = it.toInt()
                            category = when {
                                minutesReward <= 3 -> ChoreCategory.QUICK
                                minutesReward <= 7 -> ChoreCategory.SMALL
                                else -> ChoreCategory.BIG
                            }
                        },
                        valueRange = 1f..20f,
                        steps = 18
                    )
                }

                item {
                    Text("איור המשימה:", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        icons.take(4).forEach { i ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { icon = i }
                                    .background(if (icon == i) Color(0xFF4F46E5) else Color(0xFF1E293B))
                                    .padding(4.dp)
                            ) {
                                ChoreIllustration(icon = i, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        icons.drop(4).take(4).forEach { i ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { icon = i }
                                    .background(if (icon == i) Color(0xFF4F46E5) else Color(0xFF1E293B))
                                    .padding(4.dp)
                            ) {
                                ChoreIllustration(icon = i, modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val chore = initialChore?.copy(
                            title = title.trim(),
                            voiceHebrew = voiceHebrew.ifBlank { "כל הכבוד! סיימת ${title.trim()}!" },
                            minutesReward = minutesReward,
                            category = category,
                            icon = icon
                        ) ?: ChoreEntity(
                            title = title.trim(),
                            voiceHebrew = voiceHebrew.ifBlank { "כל הכבוד! סיימת ${title.trim()}!" },
                            minutesReward = minutesReward,
                            category = category,
                            icon = icon
                        )
                        onSave(chore)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
            ) {
                Text("שמור", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ביטול", color = Color(0xFF94A3B8))
            }
        }
    )
}
