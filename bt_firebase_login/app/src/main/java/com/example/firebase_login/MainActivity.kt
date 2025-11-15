package com.example.firebase_login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firebase_login.ui.LoginScreen
import com.example.firebase_login.ui.ProfileScreen
import com.example.firebase_login.ui.theme.Firebase_loginTheme
import com.example.firebase_login.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Firebase_loginTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = viewModel()
                    val user by authViewModel.currentUser.collectAsState()

                    if (user == null) {
                        LoginScreen(authViewModel = authViewModel, onSignInSuccess = {})
                    } else {
                        ProfileScreen(user = user!!, authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}
