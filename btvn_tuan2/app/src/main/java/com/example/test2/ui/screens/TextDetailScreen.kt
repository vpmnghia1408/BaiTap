package com.example.test2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun TextDetailScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)) {
        Text("Text Detail")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            buildAnnotatedString {
                append("The ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick ") }
                withStyle(SpanStyle(color = Color(0xFFB87E00), fontWeight = FontWeight.Bold)) { append("Brown ") }
                append("\nfox ")
                withStyle(SpanStyle(letterSpacing = 8.sp)) { append("jumps ") }
                withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("over ") }
                append("\nthe ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("lazy ") }
                append("dog.")
            },
            fontSize = 20.sp
        )
    }
}
