package com.danielrothmann.bookstoreapp.mainscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.auth.DeleteAccountDialog
import com.danielrothmann.bookstoreapp.auth.deleteAccountWithReauth
import com.danielrothmann.bookstoreapp.auth.signOutWithToast
import com.danielrothmann.bookstoreapp.bottommenu.BottomMenu
import com.google.firebase.auth.FirebaseAuth
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
    // Состояние для диалога удаления (Создаем состояние по умолчанию false = диалог скрыт)
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                            // диалог подтверждения на удаление аккаунта
                            showDeleteDialog = true // При клике на "Delete Account" меняем состояние на true
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        onCategoryClick = { category ->
                            // Обработка клика по категории
                            Toast.makeText(context, "Selected: $category", Toast.LENGTH_SHORT)
                                .show()
                            scope.launch {
                                drawerState.close()
                            }
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
            },
            bottomBar = {
                BottomMenu()
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
    // Диалог удаления аккаунта
    if (showDeleteDialog) { // Когда showDeleteDialog = true, диалог показывается
        DeleteAccountDialog(
            onDismiss = {
                showDeleteDialog = false // При закрытии диалога меняем состояние на false
            },
            onConfirm = { email, password ->
                auth.deleteAccountWithReauth(
                    email = email,
                    password = password,
                    context = context,
                    onSuccess = { // При успешном удалении закрываем диалог
                        showDeleteDialog = false
                        onSignOut() // Возвращаемся на экран логина
                    },
                    onFailure = { error ->
                        // Ошибка уже показана в Toast
                        // Диалог остается открытым для повторной попытки
                    }
                )
            }
        )
    }
}
