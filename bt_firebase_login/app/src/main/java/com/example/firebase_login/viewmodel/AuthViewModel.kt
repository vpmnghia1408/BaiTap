package com.example.firebase_login.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_login.R
import com.example.firebase_login.data.SignInResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _signInResult = MutableStateFlow<SignInResult>(SignInResult.Idle)
    val signInResult: StateFlow<SignInResult> = _signInResult.asStateFlow()

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<com.google.firebase.auth.FirebaseUser?> = _currentUser.asStateFlow()

    init {
        // Lắng nghe thay đổi trạng thái đăng nhập của Firebase
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun getGoogleSignInClient(context: Context) = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    fun handleGoogleSignInResult(resultCode: Int, data: android.content.Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            _signInResult.value = SignInResult.Error(Exception("Google Sign-In cancelled or failed."))
            return
        }

        _signInResult.value = SignInResult.Loading
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        viewModelScope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                if (account?.idToken != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                } else {
                    _signInResult.value = SignInResult.Error(Exception("Google ID Token is null."))
                }
            } catch (e: Exception) {
                _signInResult.value = SignInResult.Error(e)
            }
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        try {
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                _currentUser.value = user // ✅ Cập nhật UI ngay khi đăng nhập thành công
                _signInResult.value = SignInResult.Success(user)
            } else {
                _signInResult.value = SignInResult.Error(Exception("User is null after sign-in."))
            }
        } catch (e: Exception) {
            _signInResult.value = SignInResult.Error(e)
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            try {
                auth.signOut()
                getGoogleSignInClient(context).signOut().await()
                _currentUser.value = null
                _signInResult.value = SignInResult.Idle
            } catch (e: Exception) {
                println("Sign out failed: ${e.message}")
            }
        }
    }
}
