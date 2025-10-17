package com.example.test2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test2.ui.screens.WelcomeScreen
import com.example.test2.ui.screens.ComponentsListScreen
import com.example.test2.ui.screens.TextDetailScreen

// THÊM CÁC IMPORTS MỚI
import com.example.test2.ui.screens.ImagesScreen
import com.example.test2.ui.screens.TextFieldScreen
import com.example.test2.ui.screens.RowLayoutScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(onStartClick = { navController.navigate("components") })
        }

        composable("components") {
            ComponentsListScreen(
                onTextClicked = { navController.navigate("text_detail") },
                onImageClicked = { navController.navigate("images") },
                onTextFieldClicked = { navController.navigate("textfield") },
                onRowLayoutClicked = { navController.navigate("rowlayout") }
            )
        }

        composable("text_detail") {
            TextDetailScreen()
        }

        composable("images") {
            ImagesScreen(navController)
        }

        composable("textfield") {
            TextFieldScreen(navController)
        }

        composable("rowlayout") {
            RowLayoutScreen(navController)
        }
    }
}