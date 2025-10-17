package com.example.test2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ComponentsListScreen(
    onTextClicked: () -> Unit,
    onImageClicked: () -> Unit,
    onTextFieldClicked: () -> Unit,
    onRowLayoutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tiêu đề của màn hình
        Text(
            "UI Components List",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Danh sách các thành phần UI
        val items = listOf(
            "Text" to "Displays text",
            "Image" to "Displays an image",
            "TextField" to "Input field for text",
            "PasswordField" to "Input field for passwords",
            "Column" to "Arranges elements vertically",
            "Row" to "Arranges elements horizontally"
        )

        // Lặp qua từng phần tử để hiển thị
        items.forEach { (title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        // Khi click vào mỗi item, điều hướng đến màn hình tương ứng
                        when (title) {
                            "Text" -> onTextClicked()
                            "Image" -> onImageClicked()
                            "TextField" -> onTextFieldClicked()
                            "Row" -> onRowLayoutClicked()
                            // Các item khác chưa có màn hình riêng thì tạm chưa làm gì
                        }
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(desc)
                }
            }
        }
    }
}
