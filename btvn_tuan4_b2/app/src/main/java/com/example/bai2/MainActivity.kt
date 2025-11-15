// MainActivity.kt

package com.example.bai2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import com.example.bai2.ui.theme.Bai2Theme // Thay thế bằng tên theme của bạn

// ------------------- 1. Định nghĩa Routes -------------------
object Routes {
    const val FORGET_PASSWORD = "forget_password_screen"
    const val VERIFY_CODE = "verify_code_screen/{email}"
    const val RESET_PASSWORD = "reset_password_screen/{email}/{code}"
    const val CONFIRM = "confirm_screen/{email}/{code}/{password}"

    fun createVerifyCodeRoute(email: String) = "verify_code_screen/$email"
    fun createResetPasswordRoute(email: String, code: String) = "reset_password_screen/$email/$code"
    fun createConfirmRoute(email: String, code: String, password: String) = "confirm_screen/$email/$code/$password"
}

// ------------------- 2. MainActivity và NavHost -------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bai2Theme { // Đảm bảo bạn sử dụng Theme chính xác
                NavigationApp()
            }
        }
    }
}

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.FORGET_PASSWORD
    ) {
        // Màn hình 1: Nhập Email
        composable(Routes.FORGET_PASSWORD) {
            ForgetPasswordScreen(navController)
        }

        // Màn hình 2: Nhập Mã (nhận email)
        composable(
            route = Routes.VERIFY_CODE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            VerifyCodeScreen(navController, email)
        }

        // Màn hình 3: Đặt lại Mật khẩu (nhận email và code)
        composable(
            route = Routes.RESET_PASSWORD,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val code = backStackEntry.arguments?.getString("code")
            ResetPasswordScreen(navController, email, code)
        }

        // Màn hình 4: Hiển thị (nhận email, code, và password)
        composable(
            route = Routes.CONFIRM,
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            val code = backStackEntry.arguments?.getString("code")
            val password = backStackEntry.arguments?.getString("password")
            ConfirmScreen(navController, email, code, password)
        }
    }
}
// ------------------- Hỗ trợ UI -------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoScaffold(
    title: String,
    navController: NavController,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}

// ------------------- 3. Màn hình 1: ForgetPasswordScreen -------------------
@Composable
fun ForgetPasswordScreen(navController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }

    DemoScaffold(
        title = "1. Forget Password",
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("UTH SmartTasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Forget Password?", style = MaterialTheme.typography.headlineSmall)
        Text("Enter your Email, we will send you a verification code.")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isNotBlank()) {
                    // Điều hướng sang màn hình 2 và truyền email
                    navController.navigate(Routes.createVerifyCodeRoute(email))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotBlank()
        ) {
            Text("Next")
        }
    }
}

// ------------------- 4. Màn hình 2: VerifyCodeScreen -------------------
@Composable
fun VerifyCodeScreen(navController: NavController, email: String?) {
    var code by rememberSaveable { mutableStateOf("") }
    val maxCodeLength = 6

    DemoScaffold(
        title = "2. Verification",
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("UTH SmartTasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Verify Code", style = MaterialTheme.typography.headlineSmall)
        Text("Enter the code we just sent you to Email: ${email.orEmpty()}",
            softWrap = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code,
            onValueChange = {
                if (it.length <= maxCodeLength) {
                    code = it
                }
            },
            label = { Text("Enter the code (Demo)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (code.isNotBlank() && email != null) {
                    // Điều hướng sang màn hình 3, truyền email và code
                    navController.navigate(Routes.createResetPasswordRoute(email, code))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = code.isNotBlank()
        ) {
            Text("Next")
        }
    }
}

// ------------------- 5. Màn hình 3: ResetPasswordScreen -------------------
@Composable
fun ResetPasswordScreen(navController: NavController, email: String?, code: String?) {
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    DemoScaffold(
        title = "3. Reset Password",
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("UTH SmartTasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Create new password", style = MaterialTheme.typography.headlineSmall)
        Text("Your new password must be different form previously used password")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        val isPasswordValid = password.isNotBlank() && password == confirmPassword

        Button(
            onClick = {
                if (isPasswordValid && email != null && code != null) {
                    // Điều hướng sang màn hình 4, truyền email, code và password
                    navController.navigate(Routes.createConfirmRoute(email, code, password))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isPasswordValid
        ) {
            Text("Next")
        }
    }
}

// ------------------- 6. Màn hình 4: ConfirmScreen -------------------
@Composable
fun ConfirmScreen(navController: NavController, email: String?, code: String?, password: String?) {
    DemoScaffold(
        title = "4. Confirm (Dữ liệu)",
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("UTH SmartTasks", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Confirm", style = MaterialTheme.typography.headlineSmall)
        Text("We are here to help you!")
        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị dữ liệu đã truyền qua các màn hình
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("✅ Email: ${email.orEmpty()}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("✅ Mã Code: ${code.orEmpty()}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("✅ Mật khẩu: ${"*".repeat(password?.length ?: 0)}", style = MaterialTheme.typography.bodyLarge) // Che mật khẩu
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Quay về màn hình đầu tiên (ForgetPassword) và xóa hết Back Stack
                navController.popBackStack(Routes.FORGET_PASSWORD, inclusive = false)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit (Hoàn tất) - Về màn 1")
        }
    }
}

// ------------------- Preview -------------------
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Bai2Theme {
        NavigationApp()
    }
}