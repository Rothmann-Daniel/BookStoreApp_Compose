package com.danielrothmann.bookstoreapp.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danielrothmann.bookstoreapp.R
import com.danielrothmann.bookstoreapp.auth.DeleteAccountDialog
import com.danielrothmann.bookstoreapp.auth.deleteAccountWithReauth
import com.danielrothmann.bookstoreapp.auth.signOutWithToast
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit = {},
    onNavigateToAddBook: () -> Unit = {} // параметр навигации
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isAdmin by remember { mutableStateOf(false) }
    var isCheckingAdmin by remember { mutableStateOf(true) }

    // Проверяем admin статус при загрузке экрана
    LaunchedEffect(Unit) {
        AdminChecker.isAdmin { adminStatus ->
            isAdmin = adminStatus
            isCheckingAdmin = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.img_bg_mainscreen),
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Аватар с бейджем admin
            Box {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(80.dp),
                        tint = Color.White
                    )
                }

                // Бейдж "ADMIN"
                if (isAdmin) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 8.dp, y = 8.dp),
                        shape = CircleShape,
                        color = Color.Red,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "ADMIN",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Email
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Email",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = currentUser?.email ?: "Unknown",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка "Add Book" (только для админа)
            if (isAdmin) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onNavigateToAddBook, // навигация
                            indication = ripple(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Book",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Add Book",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Кнопка Sign Out
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            auth.signOutWithToast(context) {
                                onSignOut()
                            }
                        },
                        indication = ripple(color = Color.Red.copy(alpha = 0.3f)),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Sign out",
                        tint = Color.Red.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign Out",
                        fontSize = 18.sp,
                        color = Color.Red.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка Delete Account
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { showDeleteDialog = true },
                        indication = ripple(color = Color.Red.copy(alpha = 0.3f)),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete account",
                        tint = Color.Red.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Delete Account",
                        fontSize = 18.sp,
                        color = Color.Red.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Индикатор загрузки
            if (isCheckingAdmin) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            }
        }
    }

    // Диалог удаления аккаунта
    if (showDeleteDialog) {
        DeleteAccountDialog(
            onDismiss = { showDeleteDialog = false },
            onConfirm = { email, password ->
                auth.deleteAccountWithReauth(
                    email = email,
                    password = password,
                    context = context,
                    onSuccess = {
                        showDeleteDialog = false
                        onSignOut()
                    },
                    onFailure = { }
                )
            }
        )
    }
}