package com.danielrothmann.bookstoreapp.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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

@Composable
fun MainScreen(modifier: Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Open)
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                DrawerHeader()
                DrawerBody(
                    modifier = Modifier.weight(1f) // Занимает все доступное пространство
                )
                SignOutButton(
                    modifier = Modifier.padding(16.dp) // Добавляем отступы для кнопки
                ) {
                    FirebaseAuth.getInstance().signOutWithToast(context) {
                        // Здесь можно добавить навигацию на экран логина
                    }
                }
            }
        }
    ) {
        // Контент главного экрана
    }
}

// Кнопка выхода внизу
@Composable
fun SignOutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Sign out",
            tint = Color.Red.copy(alpha = 0.9f),
            modifier = Modifier.size(24.dp)
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
