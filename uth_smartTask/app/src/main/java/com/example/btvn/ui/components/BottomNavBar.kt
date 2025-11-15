package com.example.btvn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onAddClick: () -> Unit,
    onTasksClick: () -> Unit,
    onSettingsClick: () -> Unit,
    currentRoute: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        // Thanh nền bo góc
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    color = Color(0xFFF8F8F8),
                    shape = MaterialTheme.shapes.extraLarge
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentRoute == "home") Color(0xFF2196F3) else Color.Gray
                )
            }

            IconButton(onClick = onCalendarClick) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = if (currentRoute == "calendar") Color(0xFF2196F3) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(40.dp)) // chừa chỗ cho nút Add

            IconButton(onClick = onTasksClick) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Tasks",
                    tint = if (currentRoute == "tasks") Color(0xFF2196F3) else Color.Gray
                )
            }

            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = if (currentRoute == "settings") Color(0xFF2196F3) else Color.Gray
                )
            }
        }

        // Nút Add nằm nổi ở giữa
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Color(0xFF2196F3),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}
