package com.danielrothmann.bookstoreapp.mainscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.book.BookCard
import com.danielrothmann.bookstoreapp.book.CategoryRepository
import com.danielrothmann.bookstoreapp.book.getAllBooks
import com.danielrothmann.bookstoreapp.bottommenu.BottomMenu
import com.danielrothmann.bookstoreapp.data.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onSignOut: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val scope = rememberCoroutineScope()
    val categoryRepo = remember { CategoryRepository() }

    val db = remember { FirebaseFirestore.getInstance() }
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        db.getAllBooks(
            onSuccess = { result ->
                books = result
                isLoading = false
            },
            onFailure = { error ->
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.7f),
                drawerContainerColor = Color.Transparent,
                drawerContentColor = Color.White,
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    DrawerHeader()
                    DrawerBody(
                        modifier = Modifier.weight(1f),
                        categoryRepo = categoryRepo,
                        onCategoryClick = { category ->
                            Toast.makeText(context, "Selected: $category", Toast.LENGTH_SHORT).show()
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_bg_mainscreen),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Book Store", color = Color.White) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                bottomBar = {
                    BottomMenu(currentRoute = currentRoute, onNavigate = onNavigate)
                },
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        text = "Welcome to Book Store!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Cursive
                    )

                    when {
                        isLoading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                        books.isEmpty() -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Книги не найдены", color = Color.White, fontSize = 16.sp)
                            }
                        }
                        else -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items(books) { book ->
                                    BookCard(
                                        book = book,
                                        isFavorite = false,
                                        onFavoriteClick = {
                                            Toast.makeText(context, "Add to favorites", Toast.LENGTH_SHORT).show()

                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}