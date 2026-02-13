package com.danielrothmann.bookstoreapp.mainscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.danielrothmann.bookstoreapp.bottommenu.BottomMenu
import com.google.firebase.auth.FirebaseAuth
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
                        onCategoryClick = { category ->
                            Toast.makeText(context, "Selected: $category", Toast.LENGTH_SHORT).show()
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book Store") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomMenu(
                    currentRoute = currentRoute,
                    onNavigate = onNavigate
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Фоновое изображение
                Image(
                    painter = painterResource(id = R.drawable.img_bg_mainscreen_2),
                    contentDescription = "background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Затемнение
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                // Контент
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Welcome to Book Store!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Cursive
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "User: ${auth.currentUser?.email ?: "Unknown"}",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}