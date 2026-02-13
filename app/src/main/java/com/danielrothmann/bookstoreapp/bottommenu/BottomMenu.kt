package com.danielrothmann.bookstoreapp.bottommenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielrothmann.bookstoreapp.R

@Composable
fun BottomMenu() {
    val bottomMenuItems = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favorites,
        BottomMenuItem.Profile
    )

    var selectedRoute by remember { mutableStateOf("home") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.img_bt_2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            contentScale = ContentScale.Crop
        )

        // Затемняющий слой поверх изображения
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(  // Градиент снизу вверх (затемняет низ)
//                        colors = listOf(
//                            Color.Transparent,
//                            Color.DarkGray.copy(alpha = 0.5f)
//                        )
//                    )
//                )
//        )

        // Затемняющий слой поверх изображения
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .background(Color.DarkGray.copy(alpha = 0.4f))  // коэффицент затемнение
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(15.dp))  // Отступ сверху

            NavigationBar(
                containerColor = Color.Transparent
            ) {
                bottomMenuItems.forEach { item ->
                    val isSelected = selectedRoute == item.route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedRoute = item.route },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomMenuPrewiew() {
    BottomMenu()
}