package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreEntity
import com.example.data.model.ChoreHistoryEntity
import com.example.data.model.ParentSettingsEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TidyEarnRepository(private val database: AppDatabase) {
    val allChildren: Flow<List<ChildEntity>> = database.childDao().getAllChildren()
    val enabledChores: Flow<List<ChoreEntity>> = database.choreDao().getEnabledChores()
    val allChores: Flow<List<ChoreEntity>> = database.choreDao().getAllChores()
    val settingsFlow: Flow<ParentSettingsEntity?> = database.settingsDao().getSettingsFlow()
    val recentHistory: Flow<List<ChoreHistoryEntity>> = database.historyDao().getRecentHistory()

    fun getChildFlow(childId: Long): Flow<ChildEntity?> = database.childDao().getChildByIdFlow(childId)

    fun getChildHistory(childId: Long): Flow<List<ChoreHistoryEntity>> =
        database.historyDao().getHistoryForChild(childId)

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun checkAndSeedDatabase() {
        if (database.choreDao().getChoresCount() == 0 || database.childDao().getChildrenCount() == 0) {
            AppDatabase.populateDefaultData(database)
        }
    }

    suspend fun addChoreToChild(childId: Long, chore: ChoreEntity): Int {
        val child = database.childDao().getChildById(childId) ?: return 0
        val today = getTodayDateString()

        // Daily rollover check: if child's lastActiveDate is different, reset today's earned counter
        val currentTodayEarned = if (child.lastActiveDate == today) child.todayEarnedMinutes else 0

        // Cap calculation: ensure we don't exceed dailyCap
        val remainingCap = (child.dailyCapMinutes - currentTodayEarned).coerceAtLeast(0)
        val minutesToAdd = chore.minutesReward.coerceAtMost(remainingCap)

        if (minutesToAdd > 0) {
            val updatedChild = child.copy(
                todayEarnedMinutes = currentTodayEarned + minutesToAdd,
                currentBalanceMinutes = child.currentBalanceMinutes + minutesToAdd,
                lastActiveDate = today
            )
            database.childDao().updateChild(updatedChild)

            // Record history
            database.historyDao().insertHistory(
                ChoreHistoryEntity(
                    childId = child.id,
                    childName = child.name,
                    choreId = chore.id,
                    choreTitle = chore.title,
                    minutesAdded = minutesToAdd
                )
            )
        }
        return minutesToAdd
    }

    suspend fun deductMinutesFromChild(childId: Long, minutesToDeduct: Int) {
        val child = database.childDao().getChildById(childId) ?: return
        val newBalance = (child.currentBalanceMinutes - minutesToDeduct).coerceAtLeast(0)
        database.childDao().updateChild(child.copy(currentBalanceMinutes = newBalance))
    }

    suspend fun updateChildBalanceDirect(childId: Long, newBalance: Int) {
        val child = database.childDao().getChildById(childId) ?: return
        database.childDao().updateChild(child.copy(currentBalanceMinutes = newBalance.coerceAtLeast(0)))
    }

    suspend fun saveChild(child: ChildEntity): Long {
        return if (child.id == 0L) {
            database.childDao().insertChild(child.copy(lastActiveDate = getTodayDateString()))
        } else {
            database.childDao().updateChild(child)
            child.id
        }
    }

    suspend fun deleteChild(child: ChildEntity) {
        database.childDao().deleteChild(child)
        database.historyDao().clearHistoryForChild(child.id)
    }

    suspend fun saveChore(chore: ChoreEntity): Long {
        return if (chore.id == 0L) {
            database.choreDao().insertChore(chore)
        } else {
            database.choreDao().updateChore(chore)
            chore.id
        }
    }

    suspend fun deleteChore(chore: ChoreEntity) {
        database.choreDao().deleteChore(chore)
    }

    suspend fun saveSettings(settings: ParentSettingsEntity) {
        database.settingsDao().saveSettings(settings)
    }

    suspend fun getSettings(): ParentSettingsEntity {
        return database.settingsDao().getSettings() ?: ParentSettingsEntity()
    }

    suspend fun resetChildDay(childId: Long) {
        database.childDao().resetChildBalance(childId)
    }

    suspend fun resetAllToday() {
        database.childDao().resetAllTodayEarned(getTodayDateString())
    }
}
