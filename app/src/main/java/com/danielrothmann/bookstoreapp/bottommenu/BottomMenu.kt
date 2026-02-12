package com.danielrothmann.bookstoreapp.bottommenu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun BottomMenu() {
    val bottomMenuItems = listOf(
        BottomMenuItem.Home,
        BottomMenuItem.Favorites,
        BottomMenuItem.Profile
    )

    var selectedRoute by remember { mutableStateOf("home") }

    NavigationBar {
        bottomMenuItems.forEach { item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedRoute = item.route
                    // TODO: Навигация на соответствующий экран
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) {
                            item.selectedIcon  // Закрашенная иконка
                        } else {
                            item.icon          // Контурная иконка
                        },
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}