package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.screens.ChildDashboardScreen
import com.example.ui.screens.ChildSelectScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.ScreenTimeCountdownScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TidyEarnViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: TidyEarnViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
          ) {
            TidyEarnApp(viewModel = viewModel)
          }
        }
      }
    }
  }
}

@Composable
fun TidyEarnApp(viewModel: TidyEarnViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val children by viewModel.children.collectAsState()
  val enabledChores by viewModel.enabledChores.collectAsState()
  val allChores by viewModel.allChores.collectAsState()
  val settings by viewModel.settings.collectAsState()
  val recentHistory by viewModel.recentHistory.collectAsState()
  val particleTrigger by viewModel.particleTrigger.collectAsState()

  val selectedChild = viewModel.getSelectedChild()
  val isTimerRunning by viewModel.isTimerRunning.collectAsState()
  val initialTotalSec by viewModel.initialTotalSeconds.collectAsState()
  val remainingSec by viewModel.remainingSeconds.collectAsState()
  val isTimeOver by viewModel.isTimeOver.collectAsState()

  val coroutineScope = rememberCoroutineScope()

  AnimatedContent(
    targetState = currentScreen,
    transitionSpec = { fadeIn() togetherWith fadeOut() },
    label = "screen_transition"
  ) { screen ->
    when (screen) {
      AppScreen.CHILD_SELECT -> {
        ChildSelectScreen(
          children = children,
          onSelectChild = { child -> viewModel.selectChild(child) },
          onOpenParentDashboard = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) },
          verifyPin = { pin -> viewModel.verifyPin(pin) }
        )
      }

      AppScreen.CHILD_DASHBOARD -> {
        if (selectedChild != null) {
          ChildDashboardScreen(
            child = selectedChild,
            chores = enabledChores,
            onChoreTapped = { chore -> viewModel.onChoreTapped(chore) },
            onStartScreenTime = { viewModel.startScreenTime() },
            onHoldTick = { step ->
              if (settings.soundEffectsEnabled) {
                viewModel.soundManager.playHoldProgressBeep(step)
              }
            },
            onBackToChildSelect = { viewModel.navigateTo(AppScreen.CHILD_SELECT) },
            particleTrigger = particleTrigger
          )
        } else {
          ChildSelectScreen(
            children = children,
            onSelectChild = { child -> viewModel.selectChild(child) },
            onOpenParentDashboard = { viewModel.navigateTo(AppScreen.PARENT_DASHBOARD) },
            verifyPin = { pin -> viewModel.verifyPin(pin) }
          )
        }
      }

      AppScreen.COUNTDOWN_ACTIVE -> {
        if (selectedChild != null) {
          ScreenTimeCountdownScreen(
            child = selectedChild,
            totalSeconds = initialTotalSec,
            remainingSeconds = remainingSec,
            isTimerRunning = isTimerRunning,
            isTimeOver = isTimeOver,
            countdownVisualTheme = settings.countdownVisual,
            onTogglePauseResume = { viewModel.toggleTimerPauseResume() },
            onStopAndReturn = { viewModel.stopCountdownAndExit() }
          )
        }
      }

      AppScreen.PARENT_DASHBOARD -> {
        ParentDashboardScreen(
          children = children,
          chores = allChores,
          settings = settings,
          history = recentHistory,
          onSaveChild = { child -> viewModel.saveChild(child) },
          onDeleteChild = { child -> viewModel.deleteChild(child) },
          onResetChildDay = { childId -> viewModel.resetChildDay(childId) },
          onSaveChore = { chore -> viewModel.saveChore(chore) },
          onDeleteChore = { chore -> viewModel.deleteChore(chore) },
          onSaveSettings = { s -> viewModel.saveSettings(s) },
          onResetAllToday = { viewModel.resetAllChildrenToday() },
          onExitParentMode = {
            if (viewModel.selectedChildId.value != null) {
              viewModel.navigateTo(AppScreen.CHILD_DASHBOARD)
            } else {
              viewModel.navigateTo(AppScreen.CHILD_SELECT)
            }
          }
        )
      }
    }
  }
}

