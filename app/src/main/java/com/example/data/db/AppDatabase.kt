package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreCategory
import com.example.data.model.ChoreEntity
import com.example.data.model.ChoreHistoryEntity
import com.example.data.model.ChoreIcon
import com.example.data.model.ParentSettingsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ChildEntity::class,
        ChoreEntity::class,
        ChoreHistoryEntity::class,
        ParentSettingsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun choreDao(): ChoreDao
    abstract fun historyDao(): ChoreHistoryDao
    abstract fun settingsDao(): ParentSettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tidy_and_earn_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDefaultData(database)
                    }
                }
            }
        }

        suspend fun populateDefaultData(db: AppDatabase) {
            // Default Children
            val defaultChildren = listOf(
                ChildEntity(
                    id = 1,
                    name = "אורי",
                    avatarKey = "teddy",
                    colorHex = 0xFF3B82F6,
                    todayEarnedMinutes = 0,
                    currentBalanceMinutes = 10,
                    dailyCapMinutes = 60
                ),
                ChildEntity(
                    id = 2,
                    name = "מיה",
                    avatarKey = "bunny",
                    colorHex = 0xFFEC4899,
                    todayEarnedMinutes = 0,
                    currentBalanceMinutes = 15,
                    dailyCapMinutes = 60
                )
            )
            for (c in defaultChildren) {
                db.childDao().insertChild(c)
            }

            // Default Chores from Specification
            val defaultChores = listOf(
                // משימות שוטפות (2-3 דקות)
                ChoreEntity(
                    id = 1,
                    title = "זריקת עטיפה לפח",
                    voiceHebrew = "איזה יופי! זרקת עטיפה לפח!",
                    category = ChoreCategory.QUICK,
                    minutesReward = 2,
                    icon = ChoreIcon.TRASH_WRAPPER,
                    isEnabled = true,
                    orderIndex = 1
                ),
                ChoreEntity(
                    id = 2,
                    title = "הנחת בקבוק או כוס בכיור",
                    voiceHebrew = "כל הכבוד! הכוס בכיור!",
                    category = ChoreCategory.QUICK,
                    minutesReward = 3,
                    icon = ChoreIcon.CUP_SINK,
                    isEnabled = true,
                    orderIndex = 2
                ),

                // משימות קטנות (5 דקות)
                ChoreEntity(
                    id = 3,
                    title = "דובי למקום",
                    voiceHebrew = "נהדר! הדובי חזר למקום שלו!",
                    category = ChoreCategory.SMALL,
                    minutesReward = 5,
                    icon = ChoreIcon.TEDDY_ONE,
                    isEnabled = true,
                    orderIndex = 3
                ),
                ChoreEntity(
                    id = 4,
                    title = "איסוף לכלוך גדול לפח",
                    voiceHebrew = "איזה נקי! אספת לכלוך לפח!",
                    category = ChoreCategory.SMALL,
                    minutesReward = 5,
                    icon = ChoreIcon.BIG_TRASH,
                    isEnabled = true,
                    orderIndex = 4
                ),
                ChoreEntity(
                    id = 5,
                    title = "סידור המיטה",
                    voiceHebrew = "מצוין! המיטה מסודרת ויפה!",
                    category = ChoreCategory.SMALL,
                    minutesReward = 5,
                    icon = ChoreIcon.BED_TIDY,
                    isEnabled = true,
                    orderIndex = 5
                ),
                ChoreEntity(
                    id = 6,
                    title = "טיטוא חדר",
                    voiceHebrew = "אלוף! החדר נקי ומטואטא!",
                    category = ChoreCategory.SMALL,
                    minutesReward = 5,
                    icon = ChoreIcon.SWEEP_ROOM,
                    isEnabled = true,
                    orderIndex = 6
                ),
                ChoreEntity(
                    id = 7,
                    title = "סידור ספות וכריות",
                    voiceHebrew = "יופי של עבודה! הספות מסודרות!",
                    category = ChoreCategory.SMALL,
                    minutesReward = 5,
                    icon = ChoreIcon.SOFA_PILLOWS,
                    isEnabled = true,
                    orderIndex = 7
                ),

                // משימות גדולות (10 דקות)
                ChoreEntity(
                    id = 8,
                    title = "קבוצת דובים (מעל 3)",
                    voiceHebrew = "וואו! כל הבובות והדובים מסודרים!",
                    category = ChoreCategory.BIG,
                    minutesReward = 10,
                    icon = ChoreIcon.TEDDY_GROUP,
                    isEnabled = true,
                    orderIndex = 8
                ),
                ChoreEntity(
                    id = 9,
                    title = "סידור הצעצועים בחדר",
                    voiceHebrew = "איזה יופי! כל הצעצועים בארגז!",
                    category = ChoreCategory.BIG,
                    minutesReward = 10,
                    icon = ChoreIcon.TOYS_BOX,
                    isEnabled = true,
                    orderIndex = 9
                ),
                ChoreEntity(
                    id = 10,
                    title = "טיטוא סלון",
                    voiceHebrew = "איזה ניקיון! הסלון נקי ומבריק!",
                    category = ChoreCategory.BIG,
                    minutesReward = 10,
                    icon = ChoreIcon.SWEEP_LIVING,
                    isEnabled = true,
                    orderIndex = 10
                ),
                ChoreEntity(
                    id = 11,
                    title = "איסוף כל הלכלוך בסלון",
                    voiceHebrew = "מדהים! אספת את כל הדברים בסלון!",
                    category = ChoreCategory.BIG,
                    minutesReward = 10,
                    icon = ChoreIcon.LIVING_ROOM_CLEAN,
                    isEnabled = true,
                    orderIndex = 11
                )
            )
            db.choreDao().insertChores(defaultChores)

            // Default Parent Settings
            db.settingsDao().saveSettings(
                ParentSettingsEntity(
                    id = 1,
                    pinCode = "1234",
                    defaultDailyCapMinutes = 60,
                    voiceSpeechEnabled = true,
                    soundEffectsEnabled = true,
                    countdownVisual = "ICE_CREAM"
                )
            )
        }
    }
}
