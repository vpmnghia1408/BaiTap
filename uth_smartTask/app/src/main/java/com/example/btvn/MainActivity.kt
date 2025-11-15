package com.example.btvn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.btvn.service.RetrofitClient
import com.example.btvn.ui.TaskListScreen
import com.example.btvn.ui.TaskDetailScreen
import com.example.btvn.viewmodel.TaskViewModel
import com.example.btvn.viewmodel.TaskViewModelFactory
import com.example.btvn.ui.theme.BtvnTheme // 👈 đổi đúng tên Theme.kt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BtvnTheme {
                val navController = rememberNavController()
                val viewModel: TaskViewModel = viewModel(
                    factory = TaskViewModelFactory(RetrofitClient.apiService)
                )

                NavHost(
                    navController = navController,
                    startDestination = "list"
                ) {
                    composable("list") {
                        TaskListScreen(
                            viewModel = viewModel,
                            onTaskClick = { id ->
                                navController.navigate("detail/$id")
                            }
                        )
                    }

                    composable(
                        route = "detail/{id}",
                        arguments = listOf(
                            androidx.navigation.navArgument("id") {
                                type = androidx.navigation.NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        TaskDetailScreen(
                            taskId = id,
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
