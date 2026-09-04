package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ChoreCategory {
    QUICK, // 2-3 min
    SMALL, // 5 min
    BIG    // 10 min
}

enum class ChoreIcon {
    TRASH_WRAPPER,
    CUP_SINK,
    TEDDY_ONE,
    BIG_TRASH,
    BED_TIDY,
    SWEEP_ROOM,
    SOFA_PILLOWS,
    TEDDY_GROUP,
    TOYS_BOX,
    SWEEP_LIVING,
    LIVING_ROOM_CLEAN,
    PLANT_WATER,
    SHOES_RACK,
    CUSTOM
}

@Entity(tableName = "children")
data class ChildEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val avatarKey: String = "teddy", // "teddy", "lion", "bunny", "cat", "puppy", "rocket", "dino", "star"
    val colorHex: Long = 0xFF4F46E5,
    val todayEarnedMinutes: Int = 0,
    val currentBalanceMinutes: Int = 0,
    val dailyCapMinutes: Int = 60,
    val lastActiveDate: String = "" // "yyyy-MM-dd"
)

@Entity(tableName = "chores")
data class ChoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val voiceHebrew: String,
    val category: ChoreCategory,
    val minutesReward: Int,
    val icon: ChoreIcon,
    val isEnabled: Boolean = true,
    val orderIndex: Int = 0
)

@Entity(tableName = "chore_history")
data class ChoreHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val childName: String,
    val choreId: Long?,
    val choreTitle: String,
    val minutesAdded: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "parent_settings")
data class ParentSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val pinCode: String = "1234",
    val defaultDailyCapMinutes: Int = 60,
    val voiceSpeechEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val countdownVisual: String = "ICE_CREAM" // "ICE_CREAM", "BATTERY", "STAR_JAR"
)
