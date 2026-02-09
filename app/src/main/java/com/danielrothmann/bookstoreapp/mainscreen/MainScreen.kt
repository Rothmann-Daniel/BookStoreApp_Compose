package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.auth.signOutWithToast
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import com.danielrothmann.bookstoreapp.auth.deleteAccountWithReauth
import com.danielrothmann.bookstoreapp.ui.theme.backgroundDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
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
                drawerContainerColor = Color.Transparent, // Убираем белый фон
                drawerContentColor = Color.White,
                windowInsets = WindowInsets(0, 0, 0, 0) // Убирает системные отступы
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DrawerHeader()
                    DrawerBody(
                        modifier = Modifier.weight(1f),
                        onSignOut = {
                            auth.signOutWithToast(context) {
                                onSignOut()
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        onDeleteAccount = {
                            // Здесь можно добавить диалог подтверждения на удаление аккаунта

                        }
                    )
                }
            }
        }
    ) {
        // Контент главного экрана
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Book Store") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Book Store!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "User: ${auth.currentUser?.email ?: "Unknown"}",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Main content will be here",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}