package com.nokopi.stopwatchtimer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nokopi.stopwatchtimer.presentation.navigation.AppNavigation
import com.nokopi.stopwatchtimer.ui.theme.StopWatchTimerRenewalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 画面スリープ防止
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            StopWatchTimerRenewalTheme {
                AppNavigation()
            }
        }
    }
}