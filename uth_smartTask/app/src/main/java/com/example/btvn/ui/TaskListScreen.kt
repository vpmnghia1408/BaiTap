package com.example.btvn.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btvn.model.Task
import com.example.btvn.ui.components.BottomNavBar
import com.example.btvn.viewmodel.TaskViewModel
import com.example.btvn.viewmodel.TaskViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory.Factory),
    onTaskClick: (taskId: Int) -> Unit,
    onHomeClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onTasksClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    currentRoute: String = "tasks"
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { AppHeader() },
        bottomBar = {
            BottomNavBar(
                onHomeClick = onHomeClick,
                onCalendarClick = onCalendarClick,
                onAddClick = onAddClick,
                onTasksClick = onTasksClick,
                onSettingsClick = onSettingsClick,
                currentRoute = currentRoute
            )
        },
        containerColor = Color(0xFFF7F7F8)
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when {
                state.isLoading && state.tasks.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.tasks.isEmpty() -> {
                    EmptyViewCentered()
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.tasks, key = { it.id }) { task ->
                            TaskCard(task = task, onClick = { onTaskClick(task.id) })
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }

            state.errorMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp),
                    action = {}
                ) {
                    Text(msg)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppHeader() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "UTH",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF1976D2)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "SmartTasks",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF0D47A1)
                    )
                    Text(
                        "A simple and efficient to-do app",
                        fontSize = 12.sp,
                        color = Color(0xFF616161)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { /* refresh */ }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    val backgroundColor = when (task.status.lowercase(Locale.ROOT)) {
        "in progress" -> Color(0xFFF7C6CB)
        "pending" -> Color(0xFFEFF7C0)
        "completed" -> Color(0xFFD6EFD6)
        else -> Color(0xFFEDE7F6)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            val isDone = task.status.equals("completed", ignoreCase = true)
            Icon(
                if (isDone) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = Color(0xFF212121),
                modifier = Modifier.size(28.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF212121))
                Spacer(Modifier.height(6.dp))
                Text(
                    text = task.description ?: "",
                    fontSize = 13.sp,
                    color = Color(0xFF424242),
                    maxLines = 3
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Status: ${task.status}", fontWeight = FontWeight.SemiBold)
                    Text(formatDateShort(task.dueDate), color = Color(0xFF424242))
                }
            }
        }
    }
}

@Composable
fun EmptyViewCentered() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F4)),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(160.dp)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Filled.ListAlt,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFBDBDBD)
                )
                Spacer(Modifier.height(12.dp))
                Text("No Tasks Yet!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Stay productive—add something to do", color = Color(0xFF757575))
            }
        }
    }
}

private fun formatDateShort(dateString: String): String {
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val out = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        out.format(input.parse(dateString)!!)
    } catch (e: Exception) {
        dateString.take(10)
    }
}
