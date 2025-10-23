package com.example.test2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage  // <--- Quan trọng: cần thư viện Coil cho Compose
import com.example.test2.R  // <--- Thay thế theo package thực tế của bạn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Images") },
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
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // 1️⃣ Ảnh từ URL - dùng thư viện Coil
            val remoteImageUrl =
                "https://tuyensinhquocgia.com/wp-content/uploads/2023/04/Truong-Dai-hoc-Giao-thong-van-tai-TPHCM.jpg"

            AsyncImage(
                model = remoteImageUrl,
                contentDescription = "UTH Building",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.uth_placeholder)
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = remoteImageUrl,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            // 2️⃣ Ảnh trong ứng dụng (resources)
            Image(
                painter = painterResource(id = R.drawable.uth_placeholder),
                contentDescription = "Building in app",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "In app image example",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
