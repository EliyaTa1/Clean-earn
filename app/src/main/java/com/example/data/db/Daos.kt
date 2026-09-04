package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreEntity
import com.example.data.model.ChoreHistoryEntity
import com.example.data.model.ParentSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildDao {
    @Query("SELECT * FROM children ORDER BY id ASC")
    fun getAllChildren(): Flow<List<ChildEntity>>

    @Query("SELECT * FROM children WHERE id = :id")
    suspend fun getChildById(id: Long): ChildEntity?

    @Query("SELECT * FROM children WHERE id = :id")
    fun getChildByIdFlow(id: Long): Flow<ChildEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(child: ChildEntity): Long

    @Update
    suspend fun updateChild(child: ChildEntity)

    @Delete
    suspend fun deleteChild(child: ChildEntity)

    @Query("UPDATE children SET todayEarnedMinutes = 0, lastActiveDate = :todayDate")
    suspend fun resetAllTodayEarned(todayDate: String)

    @Query("UPDATE children SET todayEarnedMinutes = 0, currentBalanceMinutes = 0 WHERE id = :childId")
    suspend fun resetChildBalance(childId: Long)

    @Query("SELECT COUNT(*) FROM children")
    suspend fun getChildrenCount(): Int
}

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores ORDER BY orderIndex ASC, id ASC")
    fun getAllChores(): Flow<List<ChoreEntity>>

    @Query("SELECT * FROM chores WHERE isEnabled = 1 ORDER BY orderIndex ASC, id ASC")
    fun getEnabledChores(): Flow<List<ChoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChores(chores: List<ChoreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChore(chore: ChoreEntity): Long

    @Update
    suspend fun updateChore(chore: ChoreEntity)

    @Delete
    suspend fun deleteChore(chore: ChoreEntity)

    @Query("SELECT COUNT(*) FROM chores")
    suspend fun getChoresCount(): Int
}

@Dao
interface ChoreHistoryDao {
    @Query("SELECT * FROM chore_history ORDER BY timestamp DESC LIMIT 50")
    fun getRecentHistory(): Flow<List<ChoreHistoryEntity>>

    @Query("SELECT * FROM chore_history WHERE childId = :childId ORDER BY timestamp DESC LIMIT 30")
    fun getHistoryForChild(childId: Long): Flow<List<ChoreHistoryEntity>>

    @Insert
    suspend fun insertHistory(item: ChoreHistoryEntity)

    @Query("DELETE FROM chore_history WHERE childId = :childId")
    suspend fun clearHistoryForChild(childId: Long)
}

@Dao
interface ParentSettingsDao {
    @Query("SELECT * FROM parent_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<ParentSettingsEntity?>

    @Query("SELECT * FROM parent_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): ParentSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: ParentSettingsEntity)
}
