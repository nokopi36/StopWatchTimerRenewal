package com.nokopi.stopwatchtimer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nokopi.stopwatchtimer.presentation.settings.SettingScreen
import com.nokopi.stopwatchtimer.presentation.timer.TimerScreen

sealed class Screen(val route: String) {
    object Timer : Screen("timer")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Timer.route
    ) {
        composable(Screen.Timer.route) {
            TimerScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
