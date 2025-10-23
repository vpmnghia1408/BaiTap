package com.example.thuchanh2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

// NOTE: Thêm một thư viện để có thể dùng R.drawable.* nếu chưa có
// Bạn có thể cần import R thủ công nếu IDE không tự động thêm: import com.example.onboarding.R

// -----------------------------------------------------------
// 1. OOP: DATA MODEL (Khuôn mẫu dữ liệu)
// -----------------------------------------------------------
/**
 * Data Class dùng làm khuôn mẫu cho nội dung của một trang Onboarding.
 * Bằng cách sử dụng Data Class, bạn có thể dễ dàng tạo và quản lý nội dung
 * của 4 trang (trừ màn hình Splash).
 *
 * @param imageResId ID của tài nguyên ảnh (Drawable ID)
 * @param title Tiêu đề của trang
 * @param description Mô tả chi tiết của trang
 */
data class OnboardingPage(
    // NOTE: Cần thay thế bằng ID Drawable thực tế trong dự án của bạn
    val imageResId: Int,
    val title: String,
    val description: String
)

// -----------------------------------------------------------
// 2. DATA SOURCE (Nguồn dữ liệu)
// -----------------------------------------------------------
/**
 * Danh sách các trang Onboarding. Đây là nơi duy nhất bạn cần thay đổi
 * để cập nhật nội dung cho 3 trang Onboarding.
 */
val onboardPages = listOf(
    // Trang Onboarding 1: Quản lý thời gian
    OnboardingPage(
        // Đã thay thế placeholder bằng R.drawable.ic_onboard_1 (Cần tạo file này)
        imageResId = R.drawable.logo,
        title = "Quản lý thời gian dễ dàng",
        description = "Với công cụ quản lý tác vụ, bạn có thể dễ dàng quản lý các nhiệm vụ và thời gian của mình để làm việc hiệu quả hơn."
    ),
    // Trang Onboarding 2: Tăng hiệu suất công việc
    OnboardingPage(
        // Đã thay thế placeholder bằng R.drawable.ic_onboard_2 (Cần tạo file này)
        imageResId = R.drawable.logo,
        title = "Tăng hiệu suất công việc",
        description = "Bằng cách tập trung vào những việc quan trọng nhất, bạn có thể tối ưu hóa quy trình làm việc và luôn đạt được hiệu suất cao."
    ),
    // Trang Onboarding 3: Thông báo nhắc nhở
    OnboardingPage(
        // Đã thay thế placeholder bằng R.drawable.ic_onboard_3 (Cần tạo file này)
        imageResId = R.drawable.logo,
        title = "Thông báo nhắc nhở",
        description = "Nhận thông báo nhắc nhở kịp thời cho các nhiệm vụ sắp tới để bạn không bao giờ bỏ lỡ bất kỳ thời hạn nào."
    )
)

// -----------------------------------------------------------
// 3. NAVIGATION (Định tuyến)
// -----------------------------------------------------------
/**
 * Sealed Class định nghĩa các tuyến đường (route) cho việc điều hướng.
 * Đây là phương pháp OOP để quản lý các màn hình (trang).
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Onboarding : Screen("onboarding_screen")
    // Giữ lại Home route để không gây lỗi logic NavHost, dù không dùng đến
    object Home : Screen("home_screen")
}

// -----------------------------------------------------------
// 4. MAIN ACTIVITY & THEME
// -----------------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Sử dụng màu sắc mặc định của Material 3
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

// -----------------------------------------------------------
// 5. COMPOSABLES (Thành phần UI)
// -----------------------------------------------------------

/**
 * Quản lý luồng điều hướng của toàn bộ ứng dụng.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Onboarding.route) {
            // Truyền NavController vào OnboardingFlowScreen để có thể điều hướng lại
            OnboardingFlowScreen(navController)
        }
        // Giữ lại composable Home để không gây lỗi nếu logic Onboarding thay đổi
        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}

// --- Màn hình 1: Splash Screen (Trang chờ) ---
@Composable
fun SplashScreen(navController: NavController) {
    // State để theo dõi màn hình đã sẵn sàng chuyển tiếp chưa
    var visible by remember { mutableStateOf(false) }

    // Coroutine để tạo độ trễ 2 giây trước khi chuyển sang Onboarding
    LaunchedEffect(Unit) {
        delay(2000)
        navController.popBackStack() // Xóa splash khỏi back stack
        navController.navigate(Screen.Onboarding.route)
    }

    // Hiệu ứng Fade In cho nội dung
    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // ĐÃ CHỈNH SỬA: Thay thế placeholder bằng R.drawable.logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "UTH SmartTasks",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

// --- Quản lý luồng Onboarding (3 trang) ---
@Composable
fun OnboardingFlowScreen(navController: NavController) {
    // State để theo dõi trang Onboarding hiện tại (index: 0, 1, 2)
    var currentPageIndex by remember { mutableIntStateOf(0) }
    val isLastPage = currentPageIndex == onboardPages.size - 1
    val isFirstPage = currentPageIndex == 0

    // Lấy dữ liệu của trang hiện tại
    val pageData = onboardPages[currentPageIndex]

    // Gọi Composable chung để hiển thị nội dung
    OnboardingPageTemplate(
        pageData = pageData,
        isFirstPage = isFirstPage,
        isLastPage = isLastPage,
        onNextClick = {
            if (isLastPage) {
                // ĐÃ CHỈNH SỬA: Quay về Splash Screen để khởi động lại luồng
                navController.navigate(Screen.Splash.route) {
                    // Xóa tất cả các màn hình trong back stack (bao gồm cả Onboarding và Splash cũ)
                    // để Splash mới là màn hình duy nhất và bắt đầu lại.
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
                // Reset index cục bộ về 0 ngay lập tức (tránh lỗi nếu navigation không kịp)
                currentPageIndex = 0
            } else {
                // Chuyển sang trang tiếp theo
                currentPageIndex++
            }
        },
        onBackClick = {
            if (!isFirstPage) {
                // Quay lại trang trước
                currentPageIndex--
            }
        }
    )
}

/**
 * Composable Template (Khuôn mẫu chính) cho các trang Onboarding.
 * Đây là phần UI chung, dễ dàng tái sử dụng (Reusable Component)
 * và chỉ nhận dữ liệu từ data class (phù hợp OOP).
 *
 * @param pageData Dữ liệu OnboardingPage để hiển thị
 */
@Composable
fun OnboardingPageTemplate(
    pageData: OnboardingPage,
    isFirstPage: Boolean,
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.1f)) // Để tạo khoảng trống trên

        // 1. Image (Ảnh minh họa)
        Image(
            painter = painterResource(id = pageData.imageResId),
            contentDescription = pageData.title,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f) // Giữ tỉ lệ vuông cho ảnh
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Title (Tiêu đề)
        Text(
            text = pageData.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 28.sp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Description (Mô tả)
        Text(
            text = pageData.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f)) // Đẩy phần navigation xuống dưới

        // 4. Navigation (Phần điều hướng)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút Quay lại (Chỉ hiện từ trang 2)
            if (!isFirstPage) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_input_get), // Placeholder mũi tên trái
                        contentDescription = "Quay lại",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp)) // Giữ khoảng trống để layout không nhảy
            }

            // Nút Tiếp theo / Bắt đầu
            Button(
                onClick = onNextClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(
                    text = if (isLastPage) "Bắt Đầu" else "Tiếp theo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

// --- Màn hình 4 (Sau Onboarding - ĐÃ XÓA NỘI DUNG) ---
@Composable
fun HomeScreen() {
    // Đã loại bỏ nội dung chi tiết vì không cần dùng đến
    // Tuy nhiên, vẫn giữ lại hàm này để không bị lỗi compile cho AppNavigation
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        // Có thể để trống hoặc chỉ để một Text nhỏ
        // Text("Đã chuyển về Onboarding", color = Color.White)
    }
}

// -----------------------------------------------------------
// 6. PREVIEW
// -----------------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewOnboardingPageTemplate() {
    MaterialTheme {
        OnboardingPageTemplate(
            pageData = onboardPages[0],
            isFirstPage = true,
            isLastPage = false,
            onNextClick = {},
            onBackClick = {}
        )
    }
}
