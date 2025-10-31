package com.example.firebase_login.data

import com.google.firebase.auth.FirebaseUser

/**
 * Lớp Sealed Class để bọc kết quả đăng nhập.
 * Dùng để cập nhật trạng thái UI (thành công, lỗi, hoặc đang tải).
 */
sealed class SignInResult {
    data class Success(val user: FirebaseUser) : SignInResult()
    data class Error(val exception: Exception) : SignInResult()
    object Loading : SignInResult()
    object Idle : SignInResult() // Trạng thái chờ
}
