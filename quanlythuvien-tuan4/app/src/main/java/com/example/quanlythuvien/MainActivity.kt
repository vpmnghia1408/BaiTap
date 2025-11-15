@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.quanlythuvien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.itemsIndexed

// -------------------- OOP Models --------------------
data class Book(val id: String, val title: String)
data class Student(val id: String, val name: String)

// -------------------- Navigation --------------------
sealed class Route(val value:String) {
    data object Manage: Route("manage")
    data object Books: Route("books")
    data object Students: Route("students")
}

// -------------------- Main Activity --------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Use Material3 theme
            MaterialTheme(colorScheme = lightColorScheme()) {
                val nav = rememberNavController()
                val currentBackStack by nav.currentBackStackEntryAsState()
                val current = currentBackStack?.destination?.route ?: Route.Manage.value

                // ---------- App state (mutable & observable) ----------
                // Start with some sample data (but safe if empty)
                val books = remember {
                    mutableStateListOf(
                        Book("b1", "Sách 01"),
                        Book("b2", "Sách 02"),
                        Book("b3", "Sách 03"),
                        Book("b4", "Sách 04")
                    )
                }
                val students = remember {
                    mutableStateListOf(
                        Student("s1", "Nguyen Van A"),
                        Student("s2", "Nguyen Thi B"),
                        Student("s3", "Nguyen Van C")
                    )
                }

                // current selected student id (safe init)
                val currentStudentIdState = remember { mutableStateOf(students.firstOrNull()?.id ?: "") }

                // borrowMap: studentId -> MutableSet(bookId)
                val borrowMap: SnapshotStateMap<String, MutableSet<String>> = remember {
                    mutableStateMapOf<String, MutableSet<String>>().apply {
                        // ensure every existing student has an entry
                        students.forEach { put(it.id, mutableSetOf()) }
                        // sample borrowed data (optional)
                        put("s1", mutableSetOf("b1", "b2"))
                        put("s2", mutableSetOf("b1"))
                    }
                }

                // Keep borrowMap in sync when students list changes (e.g., add/remove)
                LaunchedEffect(students) {
                    students.forEach { st ->
                        if (!borrowMap.containsKey(st.id)) borrowMap[st.id] = mutableSetOf()
                    }
                    // if we removed students and current id no longer valid, pick first or blank
                    if (students.none { it.id == currentStudentIdState.value }) {
                        currentStudentIdState.value = students.firstOrNull()?.id ?: ""
                    }
                }

                // helper to add book / student
                fun addBookGlobal(title: String) {
                    val id = "b${books.size + 1}"
                    books.add(Book(id, title))
                }

                fun addStudentGlobal(name: String) {
                    val id = "s${students.size + 1}"
                    students.add(Student(id, name))
                    // ensure borrowMap entry exists
                    borrowMap[id] = mutableSetOf()
                    // if no current selected student, pick this new one
                    if (currentStudentIdState.value.isBlank()) currentStudentIdState.value = id
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavItem(
                                selected = current == Route.Manage.value,
                                onClick = { nav.navigate(Route.Manage.value) { launchSingleTop = true } },
                                icon = Icons.Default.Home, label = "Quản lý"
                            )
                            NavItem(
                                selected = current == Route.Books.value,
                                onClick = { nav.navigate(Route.Books.value) { launchSingleTop = true } },
                                icon = Icons.Default.Book, label = "DS Sách"
                            )
                            NavItem(
                                selected = current == Route.Students.value,
                                onClick = { nav.navigate(Route.Students.value) { launchSingleTop = true } },
                                icon = Icons.Default.Group, label = "Sinh viên"
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = nav,
                        startDestination = Route.Manage.value,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Route.Manage.value) {
                            ManageScreen(
                                students = students,
                                books = books,
                                currentStudentIdState = currentStudentIdState,
                                borrowMap = borrowMap,
                                onToggleBorrowed = { studentId, bookId, checked ->
                                    val set = borrowMap.getOrPut(studentId) { mutableSetOf() }
                                    if (checked) set.add(bookId) else set.remove(bookId)
                                    borrowMap[studentId] = set
                                }
                            )
                        }

                        composable(Route.Books.value) {
                            BooksScreen(
                                allBooks = books,
                                borrowedBy = borrowMap,
                                onAddBook = { title -> addBookGlobal(title) }
                            )
                        }

                        composable(Route.Students.value) {
                            StudentsScreen(
                                students = students,
                                currentStudentIdState = currentStudentIdState,
                                onAddStudent = { name ->
                                    addStudentGlobal(name)
                                    // if none selected, pick new one (handled in addStudentGlobal)
                                },
                                onPick = { nav.navigate(Route.Manage.value) { launchSingleTop = true } }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------- Composables --------------------
@Composable
fun RowScope.NavItem(
    selected:Boolean,
    onClick:()->Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label:String
){
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) }
    )
}

/**
 * ManageScreen
 */
@Composable
fun ManageScreen(
    students: SnapshotStateList<Student>,
    books: SnapshotStateList<Book>,
    currentStudentIdState: MutableState<String>,
    borrowMap: SnapshotStateMap<String, MutableSet<String>>,
    onToggleBorrowed: (studentId: String, bookId: String, checked: Boolean) -> Unit
) {
    // use delegate
    var currentStudentId by currentStudentIdState

    // If no students, show friendly message and return
    if (students.isEmpty()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("Chưa có sinh viên nào. Vui lòng thêm sinh viên.", color = Color.Gray)
        }
        return
    }

    // ensure currentStudentId is valid
    if (currentStudentId.isBlank() || students.none { it.id == currentStudentId }) {
        currentStudentId = students.first().id
    }

    val student = students.firstOrNull { it.id == currentStudentId } ?: Student("", "Vui lòng thêm sinh viên")
    val borrowed = borrowMap[currentStudentId] ?: mutableSetOf()

    var showAddDialog by remember { mutableStateOf(false) }
    var pickBookId by remember {
        mutableStateOf(books.firstOrNull { it.id !in borrowed }?.id ?: "")
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Hệ thống\nQuản lý Thư viện",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))

        Text("Sinh viên", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = student.name,
                onValueChange = {},
                modifier = Modifier.weight(1f),
                readOnly = true,
                singleLine = true
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = {
                    if (students.size > 1) {
                        val idx = students.indexOfFirst { it.id == currentStudentId }.let { if (it < 0) 0 else it }
                        val next = students[(idx + 1) % students.size]
                        currentStudentId = next.id
                    }
                },
                enabled = students.size > 1
            ) { Text("Thay đổi") }
        }

        Spacer(Modifier.height(18.dp))
        Text("Danh sách sách", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF0F0F0))
                .padding(12.dp)
        ) {
            if (borrowed.isEmpty()) {
                Column(
                    Modifier.fillMaxWidth().align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Bạn chưa mượn quyển sách nào", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                    Text("Nhấn 'Thêm' để bắt đầu hành trình đọc sách!", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(books.filter { it.id in borrowed }) { book ->
                        BorrowedRow(
                            title = book.title,
                            checked = true,
                            onCheckedChange = { checked ->
                                onToggleBorrowed(currentStudentId, book.id, checked)
                            }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                pickBookId = books.firstOrNull { it.id !in borrowed }?.id ?: ""
                showAddDialog = true
            },
            enabled = students.isNotEmpty() && books.any { it.id !in borrowed },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Thêm") }
    }

    if (showAddDialog) {
        val candidateBooks2 = books.filter { it.id !in borrowed }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Chọn sách để mượn") },
            text = {
                if (candidateBooks2.isEmpty()) {
                    Text("Tất cả sách đã được mượn.")
                } else {
                    Column {
                        candidateBooks2.forEach { b ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { pickBookId = b.id }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val selected = pickBookId == b.id
                                RadioButton(selected = selected, onClick = { pickBookId = b.id })
                                Spacer(Modifier.width(8.dp))
                                Text(b.title)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = students.isNotEmpty() && pickBookId.isNotEmpty() && candidateBooks2.any { it.id == pickBookId },
                    onClick = {
                        if (pickBookId.isNotEmpty()) {
                            val set = borrowMap.getOrPut(currentStudentId) { mutableSetOf() }
                            set.add(pickBookId)
                            borrowMap[currentStudentId] = set
                        }
                        showAddDialog = false
                    }
                ) { Text("Mượn") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("Hủy") } }
        )
    }
}

@Composable
fun BorrowedRow(title:String, checked:Boolean, onCheckedChange:(Boolean)->Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0x22000000), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Medium)
    }
}

/**
 * BooksScreen:
 */
@Composable
fun BooksScreen(
    allBooks: SnapshotStateList<Book>,
    borrowedBy: SnapshotStateMap<String, MutableSet<String>>,
    onAddBook: (title: String) -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Danh sách Sách", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(allBooks) { book ->
                val totalBorrowed = borrowedBy.values.count { it.contains(book.id) }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0x22000000), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(book.title, Modifier.weight(1f))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                    ) {
                        Text("Đang mượn: $totalBorrowed", Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Thêm sách") },
            text = {
                Column {
                    OutlinedTextField(value = newTitle, onValueChange = { newTitle = it }, label = { Text("Tên sách") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newTitle.isNotBlank()) {
                        onAddBook(newTitle.trim())
                        newTitle = ""
                        showAdd = false
                    }
                }) { Text("Thêm") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Hủy") } }
        )
    }
}

/**
 * StudentsScreen:
 */
@Composable
fun StudentsScreen(
    students: SnapshotStateList<Student>,
    currentStudentIdState: MutableState<String>,
    onAddStudent: (name: String) -> Unit,
    onPick: () -> Unit
) {
    var showAdd by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    val currentStudentId by currentStudentIdState

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Danh sách Sinh viên", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            IconButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add student")
            }
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            itemsIndexed(students) { _, st ->
                val selected = st.id == currentStudentId
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = .08f) else Color.White)
                        .border(1.dp, Color(0x22000000), RoundedCornerShape(12.dp))
                        .clickable {
                            currentStudentIdState.value = st.id
                            onPick()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(st.name, Modifier.weight(1f), fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
                    if (selected) {
                        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text("Đang chọn", Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }

    if (showAdd) {
        AlertDialog(
            onDismissRequest = { showAdd = false },
            title = { Text("Thêm sinh viên") },
            text = {
                Column {
                    OutlinedTextField(value = newName, onValueChange = { newName = it }, label = { Text("Tên sinh viên") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (newName.isNotBlank()) {
                        onAddStudent(newName.trim())
                        newName = ""
                        showAdd = false
                    }
                }) { Text("Thêm") }
            },
            dismissButton = { TextButton(onClick = { showAdd = false }) { Text("Hủy") } }
        )
    }
}
