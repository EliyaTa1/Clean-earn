package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffectsManager
import com.example.audio.SpeechManager
import com.example.data.db.AppDatabase
import com.example.data.model.ChildEntity
import com.example.data.model.ChoreEntity
import com.example.data.model.ChoreHistoryEntity
import com.example.data.model.ParentSettingsEntity
import com.example.data.repository.TidyEarnRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    CHILD_SELECT,
    CHILD_DASHBOARD,
    COUNTDOWN_ACTIVE,
    PARENT_DASHBOARD
}

class TidyEarnViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TidyEarnRepository
    val soundManager = SoundEffectsManager(application)
    val speechManager = SpeechManager(application)

    private val _currentScreen = MutableStateFlow(AppScreen.CHILD_SELECT)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _selectedChildId = MutableStateFlow<Long?>(null)
    val selectedChildId: StateFlow<Long?> = _selectedChildId.asStateFlow()

    private val _children = MutableStateFlow<List<ChildEntity>>(emptyList())
    val children: StateFlow<List<ChildEntity>> = _children.asStateFlow()

    val enabledChores: StateFlow<List<ChoreEntity>>
    val allChores: StateFlow<List<ChoreEntity>>
    val settings: StateFlow<ParentSettingsEntity>
    val recentHistory: StateFlow<List<ChoreHistoryEntity>>

    // Active screen time countdown state
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private val _initialTotalSeconds = MutableStateFlow(0)
    val initialTotalSeconds: StateFlow<Int> = _initialTotalSeconds.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isTimeOver = MutableStateFlow(false)
    val isTimeOver: StateFlow<Boolean> = _isTimeOver.asStateFlow()

    // Particle burst trigger
    private val _particleTrigger = MutableStateFlow(0L)
    val particleTrigger: StateFlow<Long> = _particleTrigger.asStateFlow()

    private var timerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = TidyEarnRepository(db)

        viewModelScope.launch {
            repository.allChildren.collect { list ->
                _children.value = list
            }
        }

        enabledChores = repository.enabledChores.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allChores = repository.allChores.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        settings = repository.settingsFlow
            .map { it ?: ParentSettingsEntity() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ParentSettingsEntity()
            )

        recentHistory = repository.recentHistory.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectChild(child: ChildEntity) {
        _selectedChildId.value = child.id
        _currentScreen.value = AppScreen.CHILD_DASHBOARD
        if (settings.value.voiceSpeechEnabled) {
            speechManager.speakHebrew("שלום ${child.name}! בוא נסדר ונרוויח זמן מסך!")
        }
    }

    fun getSelectedChild(): ChildEntity? {
        val id = _selectedChildId.value ?: return null
        return children.value.find { it.id == id }
    }

    /**
     * Kid taps on a chore card - Updates minutes INSTANTLY with 0ms latency!
     */
    fun onChoreTapped(chore: ChoreEntity) {
        val currentList = _children.value
        val id = _selectedChildId.value ?: return
        val child = currentList.find { it.id == id } ?: return

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTodayEarned = if (child.lastActiveDate == today) child.todayEarnedMinutes else 0
        val remainingCap = (child.dailyCapMinutes - currentTodayEarned).coerceAtLeast(0)
        val minutesToAdd = chore.minutesReward.coerceAtMost(remainingCap)

        if (minutesToAdd > 0) {
            // ⚡ 1. INSTANT SYNCHRONOUS IN-MEMORY UPDATE (Zero Delay)
            val updatedChild = child.copy(
                todayEarnedMinutes = currentTodayEarned + minutesToAdd,
                currentBalanceMinutes = child.currentBalanceMinutes + minutesToAdd,
                lastActiveDate = today
            )
            _children.value = currentList.map { if (it.id == id) updatedChild else it }

            // ⚡ 2. Instant Particle Explosion & Audio Feedback
            _particleTrigger.value = System.currentTimeMillis()

            if (settings.value.soundEffectsEnabled) {
                if (minutesToAdd >= 10 || (updatedChild.todayEarnedMinutes >= updatedChild.dailyCapMinutes)) {
                    soundManager.playCelebrationFanfare(viewModelScope)
                } else {
                    soundManager.playCoinEarnedSound(viewModelScope)
                }
            }

            if (settings.value.voiceSpeechEnabled) {
                val prompt = chore.voiceHebrew.ifBlank {
                    "כל הכבוד! הרווחת עוד $minutesToAdd דקות!"
                }
                speechManager.speakHebrew(prompt)
            }

            // ⚡ 3. Persist to Room SQLite Database in background
            viewModelScope.launch {
                repository.addChoreToChild(child.id, chore)
            }
        } else {
            // Daily cap was already full
            if (settings.value.voiceSpeechEnabled) {
                speechManager.speakHebrew("כל הכבוד! הגעת למקסימום דקות להיום!")
            }
        }
    }

    /**
     * Start the screen time countdown
     */
    fun startScreenTime() {
        val child = getSelectedChild() ?: return
        val totalSec = child.currentBalanceMinutes * 60
        if (totalSec <= 0) return

        _initialTotalSeconds.value = totalSec
        _remainingSeconds.value = totalSec
        _isTimeOver.value = false
        _isTimerRunning.value = true
        _currentScreen.value = AppScreen.COUNTDOWN_ACTIVE

        if (settings.value.soundEffectsEnabled) {
            soundManager.playLaunchSuccess(viewModelScope)
        }

        if (settings.value.voiceSpeechEnabled) {
            speechManager.speakHebrew("זמן מסך התחיל! צפייה נעימה!")
        }

        startTimerCountdown()
    }

    private fun startTimerCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                if (!_isTimerRunning.value) break

                val newSec = _remainingSeconds.value - 1
                _remainingSeconds.value = newSec

                // Sync with database every 60 seconds (1 minute elapsed)
                if (newSec % 60 == 0) {
                    val newMinutes = newSec / 60
                    _selectedChildId.value?.let { childId ->
                        repository.updateChildBalanceDirect(childId, newMinutes)
                    }
                }

                // 1 minute warning
                if (newSec == 60) {
                    if (settings.value.soundEffectsEnabled) {
                        soundManager.playWarningTick()
                    }
                    if (settings.value.voiceSpeechEnabled) {
                        speechManager.speakHebrew("נשארה דקה אחת בלבד!")
                    }
                }

                // Countdown finished (0 seconds)
                if (newSec <= 0) {
                    _isTimeOver.value = true
                    _isTimerRunning.value = false
                    _selectedChildId.value?.let { childId ->
                        // Synchronous in-memory update
                        _children.value = _children.value.map {
                            if (it.id == childId) it.copy(currentBalanceMinutes = 0) else it
                        }
                        repository.updateChildBalanceDirect(childId, 0)
                    }
                    if (settings.value.soundEffectsEnabled) {
                        soundManager.playTimeUpLullaby(viewModelScope)
                    }
                    if (settings.value.voiceSpeechEnabled) {
                        speechManager.speakHebrew("הזמן נגמר! עכשיו המסך נח. בואו נסדר שוב כדי להרוויח עוד דקות!")
                    }
                    break
                }
            }
        }
    }

    fun toggleTimerPauseResume() {
        if (_isTimeOver.value) return
        val wasRunning = _isTimerRunning.value
        _isTimerRunning.value = !wasRunning
        if (!wasRunning) {
            startTimerCountdown()
        } else {
            timerJob?.cancel()
            // Sync remaining minutes to DB on pause
            val currentMins = (_remainingSeconds.value + 59) / 60
            _selectedChildId.value?.let { childId ->
                _children.value = _children.value.map {
                    if (it.id == childId) it.copy(currentBalanceMinutes = currentMins) else it
                }
                viewModelScope.launch {
                    repository.updateChildBalanceDirect(childId, currentMins)
                }
            }
        }
    }

    fun stopCountdownAndExit() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        val currentMins = if (_isTimeOver.value) 0 else (_remainingSeconds.value + 59) / 60
        _selectedChildId.value?.let { childId ->
            _children.value = _children.value.map {
                if (it.id == childId) it.copy(currentBalanceMinutes = currentMins) else it
            }
            viewModelScope.launch {
                repository.updateChildBalanceDirect(childId, currentMins)
            }
        }
        _currentScreen.value = AppScreen.CHILD_DASHBOARD
    }

    // Parent Management Actions
    fun verifyPin(pin: String): Boolean {
        val correctPin = settings.value.pinCode.ifBlank { "1234" }
        return pin == correctPin
    }

    fun saveChild(child: ChildEntity) {
        viewModelScope.launch {
            repository.saveChild(child)
        }
    }

    fun deleteChild(child: ChildEntity) {
        viewModelScope.launch {
            repository.deleteChild(child)
            if (_selectedChildId.value == child.id) {
                _selectedChildId.value = null
                _currentScreen.value = AppScreen.CHILD_SELECT
            }
        }
    }

    fun saveChore(chore: ChoreEntity) {
        viewModelScope.launch {
            repository.saveChore(chore)
        }
    }

    fun deleteChore(chore: ChoreEntity) {
        viewModelScope.launch {
            repository.deleteChore(chore)
        }
    }

    fun saveSettings(newSettings: ParentSettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(newSettings)
        }
    }

    fun resetChildDay(childId: Long) {
        viewModelScope.launch {
            repository.resetChildDay(childId)
        }
    }

    fun resetAllChildrenToday() {
        viewModelScope.launch {
            repository.resetAllToday()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.release()
        speechManager.release()
    }
}
