package com.example.test2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowLayoutScreen(navController: NavController) {
    // Định nghĩa màu sắc theo hình ảnh
    val lightBlue = Color(0xFFBBDEFB) // Màu xanh nhạt hơn (Light Blue)
    val darkBlue = Color(0xFF42A5F5)  // Màu xanh đậm hơn (Mid Blue)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Row Layout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp), // Thêm padding ngoài
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các hàng
        ) {

            // Hàm Composable cho từng khối màu
            @Composable
            fun ColorBlock(color: Color, modifier: Modifier = Modifier) {
                Box(
                    modifier = modifier
                        .weight(1f) // Sử dụng weight để chia đều không gian
                        .padding(horizontal = 4.dp) // Khoảng cách giữa các khối trong hàng
                        .height(60.dp) // Chiều cao cố định
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                )
            }

            // Hàng 1: Xanh nhạt, Xanh đậm, Xanh nhạt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // Center cũng có thể dùng với weight
            ) {
                ColorBlock(lightBlue)
                ColorBlock(darkBlue)
                ColorBlock(lightBlue)
            }

            // Hàng 2: Xanh nhạt, Xanh đậm, Xanh nhạt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ColorBlock(lightBlue)
                ColorBlock(darkBlue)
                ColorBlock(lightBlue)
            }

            // Hàng 3: Xanh nhạt, Xanh đậm, Xanh nhạt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ColorBlock(lightBlue)
                ColorBlock(darkBlue)
                ColorBlock(lightBlue)
            }

            // Hàng 4: Xanh nhạt, Xanh đậm, Xanh nhạt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ColorBlock(lightBlue)
                ColorBlock(darkBlue)
                ColorBlock(lightBlue)
            }
        }
    }
}