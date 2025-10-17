package com.example.test2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.test2.ui.theme.Test2Theme
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Optional: extend content into system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Test2Theme {
                // gọi NavGraph
                com.example.test2.ui.navigation.AppNavigation()
            }
        }
    }
}
